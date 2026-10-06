package com.fueltracker.price;


import com.fueltracker.station.Station;
import jakarta.persistence.*;
import lombok.Getter;


import java.time.Instant;

@Entity
@Getter
public class PriceSnapshot {
    @Id
    @GeneratedValue
    private Long id;


    // With LAZY, Hibernate stores just the foreign key and only fetches the actual Station row from the DB if
    //    and when you call .getStation() and access its fields.
    // Until then, station is a lightweight proxy — not a full loaded object.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    private Double e5;
    private Double e10;
    private Double diesel;
    // status will always be "open" if priceSnapShot object is created
    private Instant timestamp;

    public PriceSnapshot() {}
    private PriceSnapshot(Builder builder) {
        this.station = builder.station;
        this.e5 = builder.e5;
        this.e10 = builder.e10;
        this.diesel = builder.diesel;

        this.timestamp = builder.timestamp;
    }

    public static class Builder {
        private Station station;
        private Double e5;
        private Double e10;
        private Double diesel;

        private Instant timestamp;

        public Builder station(Station station) {
            this.station = station;
            return this;
        }
        public Builder e5(Double e5) {
            this.e5 = e5;
            return this;
        }
        public Builder e10(Double e10) {
            this.e10 = e10;
            return this;
        }
        public Builder diesel(Double diesel) {
            this.diesel = diesel;
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public PriceSnapshot build() {return new PriceSnapshot(this);}
    }
}
