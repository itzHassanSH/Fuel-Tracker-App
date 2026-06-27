package com.fueltracker.dto;

import java.util.List;

public record SearchStationResponse(
        List<StationResponse> stations
) {
}
