package com.fueltracker.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "tankerkoenig")
public record TankerKoenigProperties(
        // Spring automatically matches tankerkoenig.api-key to the attribute apiKey here
        @Getter String apiKey,
        String baseUrl
) { }
