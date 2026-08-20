package com.fueltracker.advice;

import com.fueltracker.exceptions.LocationNotFound;
import com.fueltracker.exceptions.RateLimitExceeded;
import com.fueltracker.exceptions.StationNotFound;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(RateLimitExceeded.class)
    public ResponseEntity<@NonNull ErrorResponse> handleRateLimit(RateLimitExceeded exc) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new com.fueltracker.advice.ErrorResponse("RATE_LIMIT_EXCEEDED", exc.getMessage()));
    }

    @ExceptionHandler(LocationNotFound.class)
    public ResponseEntity<@NonNull ErrorResponse> handleLocationNotFound(LocationNotFound exc) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_CONTENT)
                // 422 — if the request was well-formed and understood, but the semantic content couldn't be resolved.
                .body(new ErrorResponse("LOCATION_NOT_PROCESSED", exc.getMessage()));
    }

    @ExceptionHandler(StationNotFound.class)
    public ResponseEntity<@NonNull ErrorResponse> handleStationNotFound(StationNotFound exc) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404 = "this specific resource, identified by ID/slug/path segment, doesn't exist."
                .body(new ErrorResponse("STATION_NOT_FOUND", exc.getMessage()));
    }
}
