package com.fueltracker.price;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.Instant;

/**
 * Save lastPrices for each station (that are scheduled) to allow insertion of unique priceSnapShots only
 */
@Entity
@Getter
public class CurrentPrice {
    @Id
    private String stationId;
    private Double e5;
    private Double e10;
    private Double diesel;
    private Instant lastChecked;

    public CurrentPrice() {};
    private CurrentPrice(Builder builder) {
        this.stationId = builder.stationId;
        this.e5 = builder.e5;
        this.e10 = builder.e10;
        this.diesel = builder.diesel;
        this.lastChecked = builder.lastChecked;
    }

    public static class Builder {
        private String stationId;
        private Double e5;
        private Double e10;
        private Double diesel;
        private Instant lastChecked;

        public CurrentPrice build () {return new CurrentPrice(this);}

        public Builder stationId(String stationId) {
            this.stationId = stationId;
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
        public Builder lastChecked(Instant lastChecked) {
            this.lastChecked = lastChecked;
            return this;
        }
    }
}
