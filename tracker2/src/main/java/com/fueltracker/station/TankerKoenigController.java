package com.fueltracker.station;

import com.fueltracker.dto.Requests.SearchStationRequest;

import com.fueltracker.dto.Responses.StationResponse;
import com.fueltracker.shared.Coordinates;
import com.fueltracker.station.service.GeocodingService;
import com.fueltracker.station.service.StationService;
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
    public ResponseEntity<List<StationResponse>> searchStations(@ModelAttribute SearchStationRequest request) {
        System.out.println("request:" + request.location()+ ", " +request.radius()+ ", " +request.fuelType()+ ", " +request.sort());
        Coordinates coordinates = geocodingService.geocode(request.location());
        System.out.println("Coordinates:" + coordinates.latitude() + ", " +  coordinates.longitude());
        return new ResponseEntity<>(tankerService.findStations(coordinates, request.radius(), request.sort(), request.fuelType()), HttpStatus.OK);
    }
}
