package com.fueltracker.station.service;

import com.fueltracker.dto.Api.ApiStation;
import com.fueltracker.dto.Api.ApiStationResponse;
import com.fueltracker.dto.Responses.StationResponse;
import com.fueltracker.shared.Coordinates;
import com.fueltracker.shared.FuelType;
import com.fueltracker.shared.SortType;
import com.fueltracker.station.Station;
import com.fueltracker.station.StationRepository;
import com.fueltracker.station.mapper.StationMapper;
import lombok.Getter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {
    private final StationRepository stationRepo;
    private final TankerKoenigClient client;
    @Getter
    private final StationMapper stationMapper;

    public StationService(TankerKoenigClient client, StationMapper mapper, StationRepository repo) {
        this.client = client;
        this.stationMapper = mapper;
        this.stationRepo = repo;
    }

//    private String buildCacheKey(Coordinates coords, int radius, SortType sort, FuelType type ) {
//        // degrees per km ≈ 0.009
//        double gridSize = (radius / 2.0) * 0.009;
//
//        double snappedLat = Math.round(coords.latitude() / gridSize) * gridSize;
//        double snappedLng = Math.round(coords.longitude() / gridSize) * gridSize;
//
//        // round to 4dp to avoid floating point noise in the key
//        snappedLat = Math.round(snappedLat * 10000.0) / 10000.0;
//        snappedLng = Math.round(snappedLng * 10000.0) / 10000.0;
//
//        return snappedLat + "," + snappedLng + ":" + radius + ":" + sort + ":" + type;
//    }

    // key = "#root.target.buildCacheKey(#coords, #radius, #sort, #type)" ,
    @Cacheable(value = "stationSearch", keyGenerator = "stationSearchKeyGenerator")
    public List<StationResponse> findStations(Coordinates coords, int radius, SortType sort, FuelType type ) {
        ApiStationResponse apiResponse = client.fetchStations(coords.latitude(), coords.longitude(), radius, sort, type);
        // Create stationResponse and stations

        // save the stations in DB upfront - the priceSnapShot however not yet (that is the job of scheduler dependent on favourited stations
        // note: since the id is the externalID we can directly saveAll - where if object already exists we simply merge,
        //       thus preventing duplicates

        List<Station> stationList = stationMapper.toDomainList(apiResponse.stations());
        stationRepo.saveAll(stationList);

        return stationMapper.toResponseList(apiResponse.stations());
        // store in cache - stationResponse directly

    }
}
