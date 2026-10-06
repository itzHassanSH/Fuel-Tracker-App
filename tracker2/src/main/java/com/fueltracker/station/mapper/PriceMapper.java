package com.fueltracker.station.mapper;

import com.fueltracker.dto.Api.ApiPrice;
import com.fueltracker.dto.Responses.RefreshResponse;
import com.fueltracker.price.CurrentPrice;
import com.fueltracker.price.PriceSnapshot;
import com.fueltracker.shared.Status;
import com.fueltracker.station.Station;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.time.Instant;


@Component
public class PriceMapper {
    public PriceSnapshot apiToDomain(ApiPrice apiPrice, Station station) {
        return new PriceSnapshot.Builder()
                .e5(apiPrice.e5())
                .e10(apiPrice.e10())
                .diesel(apiPrice.diesel())
                .station(station)
                .timestamp(Instant.now())
                .build();
    }

    public CurrentPrice snapShotToLastPrice(PriceSnapshot snapshot) {
        // Hibernate optimization where executing "getStation.getId()" lazily loads the station, via a proxy, where
        //      the proxy is built from the id - since it's the foreign key and readily available, thus this code snippet
        //          gets the id directly and doesn't need to load the entire Station row
        return new CurrentPrice.Builder()
                .e5(snapshot.getE5())
                .e10(snapshot.getE10())
                .diesel(snapshot.getDiesel())
                .lastChecked(snapshot.getTimestamp())
                .stationId(snapshot.getStation().getId())
                .build();
    }

    public RefreshResponse apiToResponse(@Nullable ApiPrice apiPrice, String stationId) {
        return new RefreshResponse(
                apiPrice == null ? Status.UNAVAILABLE : apiPrice.status().equals("open") ? Status.OPEN : apiPrice.status().equals("closed") ? Status.CLOSED : Status.NO_PRICES,
                apiPrice == null? null : apiPrice.e5(),
                apiPrice == null? null : apiPrice.e10(),
                apiPrice == null? null : apiPrice.diesel(),
                Instant.now(),
                stationId
        );
    }

}
