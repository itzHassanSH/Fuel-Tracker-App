package com.fueltracker.dto.Requests;

import com.fueltracker.shared.FuelType;
import com.fueltracker.shared.SortType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SearchStationRequest(
        // we have a single String query that we let Geo-coding API parse itself
        @NotBlank String location,
        @Min(1) @Max(25) Integer radius,
        @NotNull FuelType fuelType,
        @NotNull SortType sort
) {
}
