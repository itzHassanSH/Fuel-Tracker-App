package com.fueltracker.station;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.Instant;
import java.util.List;

@DataJpaTest
@Testcontainers
public class StationRepositoryTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18");

    @Autowired
    private StationRepository stationRepository;

    private final Station station = new Station.Builder()
            .id("ddb6c734-0dd0-48be-a53c-03fe1e94cad1")
            .name("Hessol")
            .brand("Hessol")
            .street("Ober-Eschbacher Straße")
            .postalCode("61352")
            .houseNumber("142")
            .lat(50.210686)
            .lng(8.657274)
            .isOpen(true)
            .lastSyncedAt(Instant.now())
            .build();


    @Test
    public void StationRepo_SaveAll_UpdatesExistingStations() {
        // If a station exists, and we use save-all on an object containing same stationId, then the object must get replaced nad duplicated objects of
        // same stationId may not exist
        stationRepository.saveAll(List.of(station));
        Station station2 = new Station.Builder()
                .id("ddb6c734-0dd0-48be-a53c-03fe1e94cad1")
                .name("Hessol")
                .brand("Hessol")
                .street("Ober-Eschbacher Straße")
                .postalCode("61352")
                .houseNumber("142")
                .lat(50.210686)
                .lng(8.657274)
                .isOpen(false)
                .lastSyncedAt(Instant.now())
                .build();
        stationRepository.saveAll(List.of(station2));

        List<Station> stations =
                stationRepository.findAllById(List.of(station.getId()));

        Assertions.assertThat(stations)
                .hasSize(1)
                .first()
                .extracting(Station::getId)
                .isEqualTo("ddb6c734-0dd0-48be-a53c-03fe1e94cad1");  //id of station2 and station are same - we want exactly 1 object,
        // especially station2 object should've replaced station1

        Assertions.assertThat(stations.getFirst().isOpen()).isFalse();
    }

    @Test
    public void StationRepo_FindById_FindsExactlyOneStation() {
        // We assume this was successful
        stationRepository.save(station);

        Station station1 = stationRepository.findById(station.getId()).orElseThrow();

        //This compares the objects recursively by their fields, rather than using equals()
        Assertions.assertThat(station1).usingRecursiveComparison().isEqualTo(station);
    }

    @Test
    public void StationRepo_FindById_ReturnsNullIfNoStationFound() {
        Assertions.assertThat(stationRepository.findById(station.getId())).isEmpty();
    }

}
