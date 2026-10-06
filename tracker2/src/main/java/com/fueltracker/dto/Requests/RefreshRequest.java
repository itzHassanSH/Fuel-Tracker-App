package com.fueltracker.dto.Requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record RefreshRequest(
        @NotEmpty
        @Size(max = 10, message= "at most 10 stations allowed")
        Set<
            @Pattern(
                regexp = "(?i)^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
                message = "invalid station id"
            )
        String> stationIds
) {
}
