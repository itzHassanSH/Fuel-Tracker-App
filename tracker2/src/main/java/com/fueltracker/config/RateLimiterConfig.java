package com.fueltracker.config;


import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {
    @Bean
    public RateLimiter listPhpRateLimiter() {
        return RateLimiter.create(1.0 / 60);
    }
}
