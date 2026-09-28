package com.fueltracker.advice.exceptions;

public class StationNotFound extends RuntimeException {
    public StationNotFound(String message) {
        super(message);
    }
}
