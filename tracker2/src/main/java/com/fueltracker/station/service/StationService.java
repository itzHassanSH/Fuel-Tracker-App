package com.fueltracker.station.service;

import com.fueltracker.advice.exceptions.TankerKoenigApiException;
import com.fueltracker.config.SchedulerProperties;
import com.fueltracker.dto.Api.ApiPrice;
import com.fueltracker.dto.Api.ApiPriceResponse;
import com.fueltracker.dto.Api.ApiStationResponse;
import com.fueltracker.dto.Responses.StationResponse;
import com.fueltracker.advice.exceptions.RateLimitExceeded;
import com.fueltracker.advice.exceptions.StationNotFound;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Service
public class StationService {
    private static final Logger log = LoggerFactory.getLogger(StationService.class);
    private final StationRepository stationRepo;
    private final SnapshotRepository priceRepo;
    private final CurrentPriceRepository currentPriceRepo;
    private final TankerKoenigClient client;
    @Getter
    private final StationMapper stationMapper;
    private final RateLimiter limiter;
    private final SchedulerProperties schedulerProperties;
    private final PriceMapper priceMapper;

    public StationService(TankerKoenigClient client, StationMapper mapper, StationRepository repo, @Qualifier("tankerKoenigRateLimiter") RateLimiter limiter, SnapshotRepository priceRepo,
                          CurrentPriceRepository currentPriceRepo, SchedulerProperties schedulerProperties, PriceMapper priceMapper) {
        this.client = client;
        this.stationMapper = mapper;
        this.stationRepo = repo;
        this.priceRepo = priceRepo;
        this.currentPriceRepo = currentPriceRepo;
        this.limiter = limiter;
        this.schedulerProperties = schedulerProperties;
        this.priceMapper = priceMapper;
    }


    @Cacheable(value = "stationSearch", keyGenerator = "stationSearchKeyGenerator")
    public List<StationResponse> findStations(Coordinates coords, int radius, SortType sort, FuelType type ) throws TankerKoenigApiException {
        if (!limiter.tryAcquire()) {
            throw new RateLimitExceeded("API rate limit exceeded - only 1 request per minute allowed");
        }

        ApiStationResponse apiResponse = client.fetchStations(coords.latitude(), coords.longitude(), radius, sort, type);
        // Create stationResponse and stations

        // save the stations in DB upfront - the priceSnapShot however not yet (that is the job of scheduler dependent on favourited stations
        // note: since the id is the externalID we can directly saveAll - where if object already exists we simply merge,
        //       thus preventing duplicates

        List<Station> stationList = stationMapper.toDomainList(apiResponse.stations());
        stationRepo.saveAll(stationList);
        log.debug("\ncache not used");

        return stationMapper.toResponseList(apiResponse.stations());
        // store in cache - stationResponse directly

    }

    public void schedulePrice() {
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
                        PriceSnapshot newSnapShot = new PriceSnapshot.Builder()
                                .diesel(apiPrice.diesel())
                                .e5(apiPrice.e5())
                                .e10(apiPrice.e10())
                                .station(stationRepo.findById(stationId).orElseThrow(() -> new StationNotFound(
                                        "Station: " + stationId + " not found - expected to already exist from prior search/save"
                                )))
                                .timestamp(LocalDateTime.now())
                                .build();

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
