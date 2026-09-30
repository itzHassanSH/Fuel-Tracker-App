package com.fueltracker.advice;

import com.fueltracker.advice.exceptions.LocationNotFound;
import com.fueltracker.advice.exceptions.RateLimitExceeded;
import com.fueltracker.advice.exceptions.StationNotFound;
import com.fueltracker.advice.exceptions.TankerKoenigApiException;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.validation.BindException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
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

    // pretty much not needed, since I do try and catch within scheduler method
    @ExceptionHandler(StationNotFound.class)
    public ResponseEntity<@NonNull ErrorResponse> handleStationNotFound(StationNotFound exc) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404 = "this specific resource, identified by ID/slug/path segment, doesn't exist."
                .body(new ErrorResponse("STATION_NOT_FOUND", exc.getMessage()));
    }

    @ExceptionHandler(TankerKoenigApiException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleTankerKoenigApi(TankerKoenigApiException exc) {
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY) // 502 — upstream API failed/misbehaved
                .body(new ErrorResponse("UPSTREAM_API_ERROR", "Fuel price service is currently unavailable"));
    }
    // we don't pass the raw exception message to client here, since it may contain sensitive information such as API key

    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ErrorResponse> handleException(Exception exc) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 — internal server error - handles all exceptions not already explicitly covered
                .body(new ErrorResponse("INTERNAL_SERVER_ERROR", exc.getMessage()));
    }

    // Bean Validation failures on a @Valid @ModelAttribute typically throw BindException
    @ExceptionHandler(BindException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleValidation (BindException exc) {
        // BindingResult: a container for "here's everything that went wrong, per field."
        String message = exc.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("VALIDATION_FAILED", message));
    }
}
