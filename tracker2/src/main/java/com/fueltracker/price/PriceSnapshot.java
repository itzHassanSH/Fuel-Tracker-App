package com.fueltracker.price;

import com.fueltracker.shared.FuelType;
import com.fueltracker.station.Station;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class PriceSnapshot {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    private FuelType fuelType;
    private BigDecimal price;
    private LocalDateTime timestamp;

    public PriceSnapshot() {};
    private PriceSnapshot(Builder builder) {
        this.station = builder.station;
        this.fuelType = builder.fuelType;
        this.price = builder.price;
        this.timestamp = builder.timestamp;
    }

    public static class Builder {
        private Station station;
        private FuelType fuelType;
        private BigDecimal price;
        private LocalDateTime timestamp;

        public Builder station(Station station) {
            this.station = station;
            return this;
        }
        public Builder fuelType(FuelType fuelType) {
            this.fuelType = fuelType;
            return this;
        }
        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }
        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public PriceSnapshot build() {return new PriceSnapshot(this);}
    }
}
