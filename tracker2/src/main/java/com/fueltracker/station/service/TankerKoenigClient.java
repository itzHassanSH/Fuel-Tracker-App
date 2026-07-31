package com.fueltracker.station.service;

import com.fueltracker.config.TankerKoenigProperties;
import com.fueltracker.dto.Api.ApiStationResponse;
import com.fueltracker.shared.FuelType;
import com.fueltracker.shared.SortType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.text.DecimalFormat;

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

        ApiStationResponse apiResponse =  restClient.get()
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

        System.out.println(apiResponse);
        return apiResponse;
    }
}
