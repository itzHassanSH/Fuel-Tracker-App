package com.fueltracker.advice;

public record ErrorResponse (
        String code,
        String message
) {

}
