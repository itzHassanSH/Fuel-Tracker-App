package com.fueltracker.station.mapper;

import com.fueltracker.dto.Api.ApiStation;
import com.fueltracker.dto.Responses.StationResponse;
import com.fueltracker.shared.FuelType;
import com.fueltracker.station.Station;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.time.Instant;
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
                .postalCode(postalCodeToString(apiStation.postCode()))
                .houseNumber(apiStation.houseNumber())
                .place(apiStation.place())
                .lat(apiStation.lat())
                .lng(apiStation.lng())
                .isOpen(apiStation.isOpen())
                .lastSyncedAt(Instant.now())
                .build();
        // add empty list "favouritedBy" if station doesn't exist in cache
        // add fields "lastSyncedAt" and "isActive"
    }

    public List<Station> toDomainList(List<ApiStation> apiStations) {
        return apiStations.stream().map(this::apiToDomain).toList();
    }

    public StationResponse apiToResponse(ApiStation station, FuelType type) {
        // None of the double values such as dist, diesel, e5 etc. should ever be null. Either an exception is thrown already or they exist with
        // a real value.
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

                type == FuelType.DIESEL? station.price() :station.diesel(),
                type == FuelType.E5? station.price() :station.e5(),
                type == FuelType.E10? station.price() :station.e10(),

                station.isOpen()
        );
    }

    public List<StationResponse> toResponseList(List<ApiStation> apiStations, FuelType type) {
        List<StationResponse> responses = new ArrayList<>();
        for (ApiStation station: apiStations) {
            responses.add(apiToResponse(station, type));
        }
        return responses;
    }

    private String postalCodeToString(@NonNull Integer postalCode) {
        // Assumes the Tankerkoenig-API always returns postalCodes with length <= 5 (Germany has all postal codes length 5)
        StringBuilder postalCodeString = new StringBuilder(Integer.toString(postalCode));
        while (postalCodeString.length() < 5) {
            postalCodeString.insert(0, '0');
        }
        return postalCodeString.toString();
    }
}
