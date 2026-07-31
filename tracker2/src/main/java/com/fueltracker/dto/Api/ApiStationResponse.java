package com.fueltracker.dto.Api;

import java.util.List;

public record ApiStationResponse(
        boolean ok,
        String status,
        List<ApiStation> stations,
        String message
) {
}
