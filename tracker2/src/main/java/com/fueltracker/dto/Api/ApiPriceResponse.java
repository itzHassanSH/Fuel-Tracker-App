package com.fueltracker.dto.Api;

import java.util.Map;

/**
 * @param ok
 * @param message
 * @param prices
 */
public record ApiPriceResponse (
        boolean ok,
        String message,
        Map<String, ApiPrice> prices
) {
}
