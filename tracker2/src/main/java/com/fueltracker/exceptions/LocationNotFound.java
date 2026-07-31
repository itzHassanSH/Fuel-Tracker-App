package com.fueltracker.exceptions;

public class LocationNotFound extends RuntimeException {
    public LocationNotFound(String message) {
        super(message);
    }
}
