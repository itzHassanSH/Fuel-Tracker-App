package com.fueltracker.station.service;

import com.fueltracker.advice.exceptions.RateLimitExceeded;
import com.fueltracker.dto.GeocodingApi.NominatimResponse;
import com.fueltracker.advice.exceptions.LocationNotFound;
import com.fueltracker.shared.Coordinates;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeocodingService {
    private final GeocodingClient client;
    private final RateLimiter limiter;

    public GeocodingService(GeocodingClient client, @Qualifier("nominatimRateLimiter") RateLimiter limiter) {
        this.client = client;
        this.limiter = limiter;
    }

    public Coordinates geocode(String location) {
        // Nominatim only allows 1 req per second!
        if (!limiter.tryAcquire()) {
            throw new RateLimitExceeded("Geocoding rate limit exceeded");
        }

        List<NominatimResponse> responses = client.search(location);

        if (responses == null || responses.isEmpty()) {
            throw new LocationNotFound(location);
        }

        NominatimResponse response = responses.getFirst();

        return new Coordinates(Double.parseDouble(response.lon()), Double.parseDouble(response.lat()));
    }
}
