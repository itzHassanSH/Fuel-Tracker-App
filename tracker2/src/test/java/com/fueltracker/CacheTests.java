package com.fueltracker;

import com.fueltracker.dto.Responses.StationResponse;
import com.fueltracker.shared.Coordinates;
import com.fueltracker.shared.FuelType;
import com.fueltracker.shared.SortType;
import com.fueltracker.station.StationRepository;
import com.fueltracker.station.service.GeocodingService;
import com.fueltracker.station.service.StationService;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

@SpringBootTest
public class CacheTests {
    @Autowired
    StationService service;
    @Autowired
    GeocodingService geoService;
    @Autowired
    CacheManager cacheManager;

    @MockitoBean
    StationRepository stationRepo;

    @Test
    void secondCallUsesCache() {
        Coordinates coords = geoService.geocode("Bad Homburg");

        // first call - should use API
        List<StationResponse> stationResponseList = service.findStations(coords, 5, SortType.PRICE, FuelType.DIESEL);

        // second call - should be a cache hit
        List<StationResponse> stationResponseList2 = service.findStations(coords, 5, SortType.PRICE, FuelType.DIESEL);

        CaffeineCache cache = (CaffeineCache) cacheManager.getCache("stationSearch");
        Assertions.assertNotNull(cache);
        Cache<@NonNull Object, Object> nativeCache = cache.getNativeCache();

        System.out.println(nativeCache.stats());
    }

    @AfterEach
    void clearCache() {
        cacheManager.getCache("stationSearch").clear(); // avoid state bleeding between tests
    }


}
