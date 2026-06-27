package com.fueltracker.dto;

import com.fueltracker.shared.FuelType;
import com.fueltracker.shared.SortType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SearchStationRequest(
        // we have a single String query that we let Geo-coding API parse itself
        @NotBlank String location,
        @Min(1) @Max(25) Integer radius,
        FuelType fuelType,
        SortType sort
) {
}
