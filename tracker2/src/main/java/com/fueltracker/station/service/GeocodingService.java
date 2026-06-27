package com.fueltracker.station.service;

import com.fueltracker.shared.Coordinates;
import org.springframework.stereotype.Service;

@Service
public class GeocodingService {
    public Coordinates geocode(String location);
}
