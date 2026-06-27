package com.fueltracker.dto;

public record StationResponse(
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
