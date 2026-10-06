package com.fueltracker.dto.Responses;

import com.fueltracker.shared.Status;

import java.time.Instant;

public record RefreshResponse(
    Status status,
    Double e5,
    Double e10,
    Double diesel,
    Instant fetchedAt,
    String stationId

) {
}
