package com.fueltracker.advice.exceptions;

public class LocationNotFound extends RuntimeException {
    public LocationNotFound(String message) {
        super(message);
    }
}
