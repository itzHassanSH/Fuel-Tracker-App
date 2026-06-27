package com.fueltracker.price;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnapshotRepository extends JpaRepository<@NonNull PriceSnapshot, @NonNull Long> {
}
