package com.fueltracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class TankerKoenigConfig {

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public RestClient TankerKoenigConfig(RestClient.Builder builder, TankerKoenigProperties properties) {

        return builder.baseUrl(properties.baseUrl())
                .defaultHeader("X-Api-Key", properties.apiKey())
                .build();
    }


}
