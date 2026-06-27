package com.fueltracker.station;

import com.fueltracker.dto.SearchStationRequest;
import com.fueltracker.dto.SearchStationResponse;
import com.fueltracker.shared.Coordinates;
import com.fueltracker.station.service.GeocodingService;
import com.fueltracker.station.service.StationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TankerKoenigController {
    private final StationService tankerService;
    private final GeocodingService geocodingService;

    public TankerKoenigController(StationService tankerService, GeocodingService geocodingService) {
        this.tankerService = tankerService;
        this.geocodingService = geocodingService;
    }

    // search stations by location
    @GetMapping
    public ResponseEntity<SearchStationResponse> searchStations(SearchStationRequest request) {
        Coordinates coordinates = geocodingService.geocode(request.location());
        return new ResponseEntity<>(tankerService.findStations(coordinates, request.radius(), request.sort(), request.fuelType()), HttpStatus.OK);
    }
}
