package com.fueltracker.station.service;

import com.fueltracker.dto.Api.ApiStationResponse;
import com.fueltracker.dto.SearchStationRequest;
import com.fueltracker.dto.SearchStationResponse;
import com.fueltracker.shared.Coordinates;
import com.fueltracker.shared.FuelType;
import com.fueltracker.shared.SortType;
import org.springframework.boot.data.autoconfigure.web.DataWebProperties;
import org.springframework.stereotype.Service;

@Service
public class StationService {
    private final TankerKoenigClient client;

    public StationService(TankerKoenigClient client) {
        this.client = client;
    }

    public SearchStationResponse findStations(Coordinates coords, int radius, SortType sort, FuelType type ) {
        ApiStationResponse ApiResponse = client.fetchStations(coords.latitude(), coords.longitude(), radius, sort, type);
        // Create stations
        // store in cache whatever needs to be
        // return the SearchStationResponse
    }
}
