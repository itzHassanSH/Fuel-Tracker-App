package com.fueltracker.exceptions;

public class RateLimitExceeded extends RuntimeException {
    public RateLimitExceeded(String message) {
        super(message);
    }
}
