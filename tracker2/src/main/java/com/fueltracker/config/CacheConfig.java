package com.fueltracker.config;


import com.github.benmanes.caffeine.cache.Caffeine;

// Spring's caching module ships with adapter classes
import org.springframework.cache.caffeine.CaffeineCache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;

import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;
import java.util.List;

@EnableCaching
@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(List.of(
                new CaffeineCache("stationSearch",
                        Caffeine.newBuilder()
                                .recordStats()
                                .expireAfterWrite(Duration.ofMinutes(30))
                                .maximumSize(10000)
                                .build()),
                new CaffeineCache("priceFetch", Caffeine.newBuilder()
                        .recordStats()
                        .expireAfterWrite(Duration.ofHours(2))
                        .maximumSize(10000)
                        .build())
                )
        );
        return manager;
    }
}
