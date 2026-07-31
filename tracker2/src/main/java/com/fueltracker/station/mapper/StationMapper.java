package com.fueltracker.station.mapper;

import com.fueltracker.dto.Api.ApiStation;
import com.fueltracker.dto.Responses.StationResponse;
import com.fueltracker.station.Station;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class StationMapper {
    public Station apiToDomain(ApiStation apiStation) {
        // use builder
        return new Station.Builder()
                .id(apiStation.id())
                .name(apiStation.name())
                .brand(apiStation.brand())
                .street(apiStation.street())
                .postalCode(apiStation.postCode())
                .houseNumber(apiStation.houseNumber())
                .place(apiStation.place())
                .lat(apiStation.lat())
                .lng(apiStation.lng())
                .distance(apiStation.dist())
                .favouritedBy(new ArrayList<>())
                .isActive(apiStation.isOpen())
                .lastSyncedAt(LocalDateTime.now())
                .build();
        // add empty list "favouritedBy" if station doesnt exist in cache
        // add fields "lastSyncedAt" and "isActive"
    }

    public List<Station> toDomainList(List<ApiStation> apiStations) {
        return apiStations.stream().map(this::apiToDomain).toList();
    }

    public StationResponse apiToResponse(ApiStation station) {
        return new StationResponse(
                station.name(),
                station.brand(),
                station.id(),
                station.street(),
                Integer.toString(station.postCode()),
                station.houseNumber(),
                station.place(),
                station.dist(),

                station.lat(),
                station.lng(),

                station.diesel(),
                station.e5(),
                station.e10(),

                station.isOpen()
        );
    }

    public List<StationResponse> toResponseList(List<ApiStation> apiStations) {
        return apiStations.stream().map(this::apiToResponse).toList();
    }
}
