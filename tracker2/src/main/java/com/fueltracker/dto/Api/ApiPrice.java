package com.fueltracker.dto.Api;

import com.fueltracker.component.FalseAsNullDeserializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tools.jackson.databind.annotation.JsonDeserialize;


/**
 * Represents a Price object from the TankerKoenig Api.
 * Each station in request gets a price object returned attached to it via Map structure
 * @param status
 * @param e5
 * @param e10
 * @param diesel
 */
public record ApiPrice(
        @NotNull @NotBlank String status,  // either "open" or "closed or "no prices"

        @JsonDeserialize(using = FalseAsNullDeserializer.class)
        Double e5,

        @JsonDeserialize(using = FalseAsNullDeserializer.class)
        Double e10,

        @JsonDeserialize(using = FalseAsNullDeserializer.class)
        Double diesel
) {
}
