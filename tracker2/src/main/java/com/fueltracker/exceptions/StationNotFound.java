package com.fueltracker.exceptions;

public class StationNotFound extends RuntimeException {
    public StationNotFound(String message) {
        super(message);
    }
}
