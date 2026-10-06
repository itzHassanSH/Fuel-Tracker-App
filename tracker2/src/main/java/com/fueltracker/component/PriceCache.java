package com.fueltracker.component;

import com.fueltracker.dto.Responses.RefreshResponse;
import lombok.NonNull;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Component
public class PriceCache {
    private final Cache cache;

    public PriceCache(CacheManager cacheManager) {
        this.cache = Objects.requireNonNull(
                cacheManager.getCache("priceFetch"),
                "priceFetch cache is not configured"
        );
    }

    /** Returns the cached entry, or null if absent or evicted. */
    public Optional<RefreshResponse> get(String stationId) {
        return Optional.ofNullable(cache.get(stationId, RefreshResponse.class));
    }

    public void put(RefreshResponse resp) {
        cache.put(resp.stationId(), resp);
    }

    /** Checks if the cached result has been in cache longer than window */
    public boolean isFresh(@NonNull RefreshResponse r, Duration window) {
        Instant cutoff = Instant.now().minus(window);
        return r.fetchedAt().isAfter(cutoff);
    }
}
