package com.fueltracker.dto.Requests;

import com.fueltracker.shared.FuelType;
import com.fueltracker.shared.SortType;
import com.fueltracker.validation.AllowedRadius;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SearchStationRequest(
        // we have a single String query that we let Geo-coding API parse itself
        @NotBlank String location,
        // AllowedRadius makes sure radius is exactly one of the 1,5,...,25 values and not a range
        @NotNull @AllowedRadius Integer radius,
        @NotNull FuelType fuelType,
        @NotNull SortType sort
) {
}
