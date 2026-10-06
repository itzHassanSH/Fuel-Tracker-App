package com.fueltracker.station.service;

import com.fueltracker.advice.exceptions.TankerKoenigApiException;
import com.fueltracker.component.PriceCache;
import com.fueltracker.config.SchedulerProperties;
import com.fueltracker.dto.Api.ApiPrice;
import com.fueltracker.dto.Api.ApiPriceResponse;
import com.fueltracker.dto.Api.ApiStationResponse;
import com.fueltracker.dto.Responses.StationResponse;
import com.fueltracker.advice.exceptions.RateLimitExceeded;
import com.fueltracker.advice.exceptions.StationNotFound;
import com.fueltracker.dto.Responses.RefreshResponse;
import com.fueltracker.price.CurrentPrice;
import com.fueltracker.price.CurrentPriceRepository;
import com.fueltracker.price.PriceSnapshot;
import com.fueltracker.price.SnapshotRepository;
import com.fueltracker.shared.Coordinates;
import com.fueltracker.shared.FuelType;
import com.fueltracker.shared.SortType;
import com.fueltracker.station.Station;
import com.fueltracker.station.StationRepository;
import com.fueltracker.station.mapper.PriceMapper;
import com.fueltracker.station.mapper.StationMapper;
import com.google.common.util.concurrent.RateLimiter;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.time.Duration;
import java.util.*;


@Service
public class StationService {
    private static final Logger log = LoggerFactory.getLogger(StationService.class);
    private final StationRepository stationRepo;
    private final SnapshotRepository priceRepo;
    private final CurrentPriceRepository currentPriceRepo;
    private final TankerKoenigClient client;
    @Getter
    private final StationMapper stationMapper;
    private final RateLimiter stationLimiter;
    private final RateLimiter pricesLimiter;
    private final SchedulerProperties schedulerProperties;
    private final PriceMapper priceMapper;
    private final PriceCache priceCache;
    private static final Duration FRESH_FOR = Duration.ofMinutes(10);

    public StationService(TankerKoenigClient client, StationMapper mapper, StationRepository repo, @Qualifier("tankerKoenigRateLimiter") RateLimiter stationLimiter, @Qualifier("pricesRateLimiter") RateLimiter priceLimiter,
                          SnapshotRepository priceRepo, CurrentPriceRepository currentPriceRepo, SchedulerProperties schedulerProperties, PriceMapper priceMapper, PriceCache priceCache) {
        this.client = client;
        this.stationMapper = mapper;
        this.stationRepo = repo;
        this.priceRepo = priceRepo;
        this.currentPriceRepo = currentPriceRepo;
        this.stationLimiter = stationLimiter;
        this.schedulerProperties = schedulerProperties;
        this.priceMapper = priceMapper;
        this.pricesLimiter = priceLimiter;
        this.priceCache = priceCache;

    }


    @Cacheable(value = "stationSearch", keyGenerator = "stationSearchKeyGenerator")
    public List<StationResponse> findStations(Coordinates coords, int radius, SortType sort, FuelType type ) {
        if (!stationLimiter.tryAcquire()) {
            throw new RateLimitExceeded("Stations API rate limit exceeded - only 1 request per 30 seconds allowed");
        }

        ApiStationResponse apiResponse = client.fetchStations(coords.latitude(), coords.longitude(), radius, sort, type);
        // Create stationResponse and stations

        // save the stations in DB upfront - the priceSnapShot however not yet (that is the job of scheduler dependent on favourited stations
        // note: since the id is the externalID we can directly saveAll - where if object already exists we simply merge,
        //       thus preventing duplicates

        // To prevent different objects with same id being saved into DB, we only save when the fuelType is all, and not a single specific one
        if (type.equals(FuelType.ALL)) {
            List<Station> stationList = stationMapper.toDomainList(apiResponse.stations());
            stationRepo.saveAll(stationList);
        }
        log.debug("\ncache not used");

        return stationMapper.toResponseList(apiResponse.stations(), type);
        // store in cache - stationResponse directly

    }

    public List<RefreshResponse> refresh(Set<String> stationIds) {
        // DONE: implement cache logic with splitting between hit-and-miss - naturally before calling api

        List<RefreshResponse> result = new ArrayList<>();
        List<String> stale = new ArrayList<>();

        // hit : if present in cache (i.e. not null) AND not more than 10 minutes old
        for (String id : stationIds) {
            Optional<RefreshResponse> cached = priceCache.get(id);
            if (cached.isPresent() && priceCache.isFresh(cached.get(), FRESH_FOR)) {
                result.add(cached.get());
            } else {
                stale.add(id);
            }
        }
        if (stale.isEmpty()) return result;

        if (!pricesLimiter.tryAcquire()) {
            for (String id: stale) {
                priceCache.get(id).ifPresent(result::add);
            }
            if (result.isEmpty()) throw new RateLimitExceeded("Please try again later - rate limit exceeded and no cached results available");
            return result;
        }

        List<RefreshResponse> responseList = new ArrayList<>();
        ApiPriceResponse apiPrices = client.fetchPrices(stationIds.stream().toList());
        for (String stationId: stationIds.stream().toList()) {
            ApiPrice price = apiPrices.prices().get(stationId);
            // In case of price being null, it creates an empty response (all prices are null) with Status: UNAVAILABLE
            RefreshResponse resp = priceMapper.apiToResponse(price, stationId);
            if (price != null) {
                priceCache.put(resp);
            }
            responseList.add(resp);
        }

        return responseList;
    }

    public void schedulePrice() {
        double waited = pricesLimiter.acquire();   // sleeps here if needed, then continues
        if (waited > 0) {
            log.info("Scheduler waited {}s for rate limiter", waited);
        }
        List<String> stationIds = schedulerProperties.getStationIds();
        ApiPriceResponse apiPrices;
        try {
            apiPrices = client.fetchPrices(stationIds);
        } catch (TankerKoenigApiException | RestClientException e) {
            log.error("Price fetch failed, skipping this run: {}", e.getMessage());
            return;
        }

        for (String stationId: stationIds) {

            ApiPrice apiPrice = apiPrices.prices().get(stationId);  // can return null - must be handled
            if (apiPrice == null) {
                log.warn("No price data returned for station {}", stationId);
                continue;
            }

            if ("open".equals(apiPrice.status())) {
                CurrentPrice lastPrice = currentPriceRepo.findById(stationId).orElse(null);
                if (lastPrice == null
                    || !Objects.equals(lastPrice.getE5(), apiPrice.e5())
                    || !Objects.equals(lastPrice.getE10(), apiPrice.e10())
                    || !Objects.equals(lastPrice.getDiesel(), apiPrice.diesel()))
                {
                    try {
                        PriceSnapshot newSnapShot = priceMapper.apiToDomain(
                                apiPrice,
                                stationRepo.findById(stationId).orElseThrow(() -> new StationNotFound(
                                "Station: " + stationId + " not found - expected to already exist from prior search/save"))
                        );
                        priceRepo.save(newSnapShot);
                        currentPriceRepo.save(priceMapper.snapShotToLastPrice(newSnapShot));
                    } catch (StationNotFound e){
                        log.warn("Failed to process station {}: {}", stationId, e.getMessage(), e);
                    }

                }
            }


        }

    }
}
