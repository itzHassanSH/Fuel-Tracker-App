package com.fueltracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class NominatimConfig {
    @Bean("nominatimRestClient")
    public RestClient nominatimRestClient(RestClient.Builder builder, NominatimProperties properties) {

        return builder.baseUrl(properties.getBaseUrl())
                .defaultHeader("User-Agent", "FuelTracker")
                .build();
    }
}
