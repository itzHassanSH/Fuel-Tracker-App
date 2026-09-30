package com.fueltracker.component;

import com.fueltracker.shared.Coordinates;
import com.fueltracker.shared.FuelType;
import com.fueltracker.shared.SortType;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component("stationSearchKeyGenerator")
public class StationSearchKeyGenerator implements KeyGenerator {
    private static final Logger log = LoggerFactory.getLogger(StationSearchKeyGenerator.class);

    @Override
    public Object generate(Object target, Method method, @Nullable Object... params) {
        Coordinates coords = (Coordinates) params[0];
        int radius = (int) params[1];
        SortType sort = (com.fueltracker.shared.SortType) params[2];
        FuelType type = (FuelType) params[3];

        // degrees per km ≈ 0.009
        double gridSize = (radius / 4.0) * Math.sqrt(2) * 0.009;

        double snappedLat = Math.round(coords.latitude() / gridSize) * gridSize;
        double snappedLng = Math.round(coords.longitude() / gridSize) * gridSize;

        // round to 4dp to avoid floating point noise in the key
        snappedLat = Math.round(snappedLat * 10000.0) / 10000.0;
        snappedLng = Math.round(snappedLng * 10000.0) / 10000.0;

        log.debug("{},{}:{}:{}:{}", snappedLat, snappedLng, radius, sort, type);
        return snappedLat + "," + snappedLng + ":" + radius + ":" + sort + ":" + type;
    }
}
