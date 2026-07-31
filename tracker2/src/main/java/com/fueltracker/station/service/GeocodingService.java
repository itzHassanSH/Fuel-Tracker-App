package com.fueltracker.station.service;

import com.fueltracker.dto.GeocodingApi.NominatimResponse;
import com.fueltracker.exceptions.LocationNotFound;
import com.fueltracker.shared.Coordinates;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeocodingService {
    private final GeocodingClient client;

    public GeocodingService(GeocodingClient client) {
        this.client = client;
    }

    public Coordinates geocode(String location) {
        List<NominatimResponse> responses = client.search(location);

        if (responses.isEmpty()) {
            throw new LocationNotFound(location);
        }

        NominatimResponse response = responses.getFirst();

        return new Coordinates(Double.parseDouble(response.lon()), Double.parseDouble(response.lat()));
    }
}
