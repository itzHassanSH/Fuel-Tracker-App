package com.fueltracker.advice.exceptions;

public class RateLimitExceeded extends RuntimeException {
    public RateLimitExceeded(String message) {
        super(message);
    }
}
