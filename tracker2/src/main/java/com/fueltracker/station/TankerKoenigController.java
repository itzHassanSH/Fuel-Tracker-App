package com.fueltracker.station;

import com.fueltracker.dto.Requests.RefreshRequest;
import com.fueltracker.dto.Requests.SearchStationRequest;

import com.fueltracker.dto.Responses.StationResponse;
import com.fueltracker.dto.Responses.RefreshResponse;
import com.fueltracker.shared.Coordinates;
import com.fueltracker.station.service.GeocodingService;
import com.fueltracker.station.service.StationService;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class TankerKoenigController {
    private final StationService tankerService;
    private final GeocodingService geocodingService;

    public TankerKoenigController(StationService tankerService, GeocodingService geocodingService) {
        this.tankerService = tankerService;
        this.geocodingService = geocodingService;
    }

    // search stations by location
    @GetMapping("search/stations")
    public ResponseEntity<@NonNull List<StationResponse>> searchStations(@ModelAttribute  @Valid SearchStationRequest request) {
        // Since we first need coordinates to even check cache, Nominatim requires its own rate limiter
        Coordinates coordinates = geocodingService.geocode(request.location());

        return new ResponseEntity<>(tankerService.findStations(coordinates, request.radius(), request.sort(), request.fuelType()), HttpStatus.OK);
    }

    // get refreshed favourite stations
    @GetMapping("favourites/refresh")
    public ResponseEntity<@NonNull List<RefreshResponse>> fetchStations(@ModelAttribute @Valid RefreshRequest request) {
        return new ResponseEntity<>(tankerService.refresh(request.stationIds()), HttpStatus.OK);
    }
}
