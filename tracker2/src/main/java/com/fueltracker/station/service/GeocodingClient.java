package com.fueltracker.station.service;

import com.fueltracker.dto.GeocodingApi.NominatimResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class GeocodingClient {
    private final RestClient restClient;

    public GeocodingClient( @Qualifier("nominatimRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public List<NominatimResponse> search(String location) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", location)
                        .queryParam("format", "jsonv2")
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<NominatimResponse>>() {});
    }

}
