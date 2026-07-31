package com.fueltracker.dto.Responses;

public record StationResponse(
    String name,
    String brand,
    String externalId,

    String street,
    String postalCode,
    String houseNumber,
    String place,  // E.g. Stuttgart

    double distance,
    double lat,
    double lng,

    Double diesel,
    Double e5,
    Double e10,

    boolean isOpen
) {

}
