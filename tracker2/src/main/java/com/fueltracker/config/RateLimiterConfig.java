package com.fueltracker.config;


import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {
    @Bean("tankerKoenigRateLimiter")
    public RateLimiter listPhpRateLimiter() {
        return RateLimiter.create(1.0 / 60);
    }

    @Bean("nominatimRateLimiter")
    public RateLimiter nominatimRateLimiter() { return RateLimiter.create(1.0);}

    // Required for fetch prices subroutine - allow favourited stations to be reloaded while not overwhelming api
    @Bean("pricesRateLimiter")
    public RateLimiter pricesPhpRateLimiter() {return RateLimiter.create(1.0 / 30);}
}
