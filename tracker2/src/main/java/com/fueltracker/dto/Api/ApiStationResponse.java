package com.fueltracker.dto.Api;

import java.util.List;

public record ApiStationResponse(
        List<ApiStation> stations,
        boolean ok,
        String status
) {
}
