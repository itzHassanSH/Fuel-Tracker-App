package com.fueltracker.station;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavouriteStationRepository extends JpaRepository<@NonNull FavouriteStation, @NonNull Long> {
}
