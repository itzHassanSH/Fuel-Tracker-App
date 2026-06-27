package com.fueltracker.station.service;

import com.fueltracker.config.TankerKoenigProperties;
import com.fueltracker.dto.Api.ApiStationResponse;
import com.fueltracker.shared.FuelType;
import com.fueltracker.shared.SortType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TankerKoenigClient {
    private final RestClient restClient;
    private final TankerKoenigProperties properties;

    public TankerKoenigClient(RestClient restClient, TankerKoenigProperties properties) {
        this.restClient = restClient;
        this.properties = properties;
    }

    public ApiStationResponse fetchStations(double lat, double lng, int radius, SortType sortType, FuelType fuelType) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stations/json")
                        .queryParam("lat", lat)
                        .queryParam("lng", lng)
                        .queryParam("rad", radius)
                        .queryParam("sort", sortType)
                        .queryParam("type", fuelType)
                        .queryParam("apikey", properties.getApiKey())
                        .build())
                .retrieve()
                .body(ApiStationResponse.class);

    }
}
