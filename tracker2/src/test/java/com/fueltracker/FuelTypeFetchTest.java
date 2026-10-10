package com.fueltracker;

import com.fueltracker.dto.Responses.StationResponse;
import com.fueltracker.shared.Coordinates;
import com.fueltracker.shared.FuelType;
import com.fueltracker.shared.SortType;
import com.fueltracker.station.service.GeocodingService;
import com.fueltracker.station.service.StationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class FuelTypeFetchTest {
    @Autowired
    StationService service;
    @Autowired
    GeocodingService geoService;

    @Test
    public void singleType() {
        Coordinates coords = geoService.geocode("Bad Homburg, 61352");

        // FuelType: Diesel - only diesel() should hold a value, rest should be null
        List<StationResponse> stationResponseList = service.findStations(coords, 5, SortType.PRICE, FuelType.DIESEL);

        for (StationResponse resp: stationResponseList) {
            Assertions.assertNotNull(resp.diesel());
            Assertions.assertNull(resp.e5());
            Assertions.assertNull(resp.e10());
        }
    }

}
