package com.fueltracker.config;


import com.github.benmanes.caffeine.cache.Caffeine;

// Spring's caching module ships with adapter classes
import org.springframework.cache.caffeine.CaffeineCacheManager;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@EnableCaching
@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager("stationSearch");
        manager.setCaffeine(Caffeine.newBuilder()
                .recordStats()
                .expireAfterWrite(Duration.ofMinutes(30))
                .maximumSize(10000));
        return manager;
    }
}
