package com.fueltracker.station.service;

import com.fueltracker.advice.exceptions.TankerKoenigApiException;
import com.fueltracker.config.TankerKoenigProperties;
import com.fueltracker.dto.Api.ApiPriceResponse;
import com.fueltracker.dto.Api.ApiStationResponse;
import com.fueltracker.shared.FuelType;
import com.fueltracker.shared.SortType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


import java.util.List;

@Component
public class TankerKoenigClient {
    private final RestClient restClient;
    private final TankerKoenigProperties properties;

    public TankerKoenigClient(@Qualifier ("tankerKoenigRestClient") RestClient restClient, TankerKoenigProperties properties) {
        this.restClient = restClient;
        this.properties = properties;
    }

    public ApiStationResponse fetchStations(double lat, double lng, int radius, SortType sortType, FuelType fuelType) {

        String sortTypeString = sortType.toString().toLowerCase();
        String fuelTypeString = fuelType.toString().toLowerCase();

        ApiStationResponse resp =  restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/json/list.php")
                        .queryParam("lat", lat)
                        .queryParam("lng", lng)
                        .queryParam("rad", radius)
                        .queryParam("sort", fuelTypeString.equals("all")? "dist" : sortTypeString)
                        .queryParam("type", fuelTypeString)
                        .queryParam("apikey", properties.getApiKey())
                        .build())
                .retrieve()
                .body(ApiStationResponse.class);

        // Exception caught
        if (resp == null || !resp.ok()) {
            throw new TankerKoenigApiException(resp == null? "empty response" : resp.message());
        }
        return resp;
    }

    public ApiPriceResponse fetchPrices(List<String> stationIds) {
        StringBuilder stringBuilder = new StringBuilder(stationIds.getFirst());
        for (int i = 1; i < stationIds.size(); i++) {
            stringBuilder.append(",").append(stationIds.get(i));
        }

        ApiPriceResponse resp = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/json/prices.php")
                        .queryParam("ids", stringBuilder.toString())
                        .queryParam("apikey", properties.getApiKey())
                        .build())
                .retrieve()
                .body(ApiPriceResponse.class);

        // Exception then caught at scheduler
        if (resp == null || !resp.ok()) {
            throw new TankerKoenigApiException(resp == null? "empty response" : resp.message());
        }
        return resp;
    }
}
