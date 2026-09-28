package com.fueltracker.price;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentPriceRepository extends JpaRepository<@NonNull CurrentPrice, @NonNull String> {
}
