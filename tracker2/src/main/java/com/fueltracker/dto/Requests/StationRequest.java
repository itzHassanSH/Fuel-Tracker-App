package com.fueltracker.dto.Requests;

public record StationRequest(
        String name,
        String brand,
        String street,
        String postalCode,
        int houseNumber,
        String place,  // E.g. Stuttgart
        double lat,
        double lng,
        float distance
) {
}
