package com.fueltracker.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "geocoding")
public record NominatimProperties (
        @Getter String baseUrl
){

}
