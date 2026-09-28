package com.fueltracker.station.mapper;

import com.fueltracker.dto.Api.ApiPrice;
import com.fueltracker.price.CurrentPrice;
import com.fueltracker.price.PriceSnapshot;
import com.fueltracker.station.Station;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;


@Component
public class PriceMapper {
    public PriceSnapshot apiToDomain(ApiPrice apiPrice, Station station) {
        return new PriceSnapshot.Builder()
                .e5(apiPrice.e5())
                .e10(apiPrice.e10())
                .diesel(apiPrice.diesel())
                .station(station)
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
                .lastChecked(snapshot.getTimestamp().toInstant(ZoneOffset.MIN))
                .stationId(snapshot.getStation().getId())
                .build();
    }

}
