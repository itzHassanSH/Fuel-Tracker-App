package com.fueltracker.dto.GeocodingApi;

public record NominatimResponse(
        String lat,
        String lon,
        String display_name
) {
}
