package com.fueltracker.station;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.Instant;

// TODO: change postalCode's type from INT to STRING, since post codes like Dresden's start with 0 thus ends up being 1705 instead of 01705
// TODO: need logic to convert such integer (from api) to correct string too (use length check)

@Getter
@Entity
public class Station {
    @Id
    private String id;

    private String name;
    private String brand;
    private String street;
    private String postalCode;
    private String houseNumber;
    private String place;   // E.g. Stuttgart

    // Coordinates
    private double lat;
    private double lng;

    // removed field: "distance"
    private boolean isOpen;
    private Instant lastSyncedAt;

    // both opening times and fuels should lie inside cache and returned after requests -
    // just not saved inside the DB itself

    // here no cascading since deleting a favorite should NOT delete the station itself
    // Removed favouritedBy field

    // No OneToMany direction with price snapshot!
    // Since most of the time we'll be doing priceSnapshotRepo.findBy...
    // We'll never need station.getSnapshots()

    public Station() {};
    private Station(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.brand = builder.brand;
        this.street = builder.street;
        this.postalCode = builder.postalCode;
        this.houseNumber = builder.houseNumber;
        this.place = builder.place;
        this.lat = builder.lat;
        this.lng = builder.lng;
        this.isOpen = builder.isOpen;
        this.lastSyncedAt = builder.lastSyncedAt;
    }

    public static class Builder {
        private String id;
        private String name;
        private String brand;
        private String street;
        private String postalCode;
        private String houseNumber;
        private String place;   // E.g. Stuttgart
        private double lat;
        private double lng;
        private boolean isOpen;
        private Instant lastSyncedAt;

        public Builder id (String id) {
            this.id = id;
            return this;
        }
        public Builder name (String name) {
            this.name = name;
            return this;
        }
        public Builder brand (String brand) {
            this.brand = brand;
            return this;
        }
        public Builder street (String street) {
            this.street = street;
            return this;
        }
        public Builder postalCode (String postalCode) {
            this.postalCode = postalCode;
            return this;
        }
        public Builder houseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
            return this;
        }
        public Builder place (String place) {
            this.place = place;
            return this;
        }
        public Builder lat (double lat) {
            this.lat = lat;
            return this;
        }
        public Builder lng (double lng) {
            this.lng = lng;
            return this;
        }
        public Builder isOpen(boolean isOpen) {
            this.isOpen = isOpen;
            return this;
        }
        public Builder lastSyncedAt(Instant lastSyncedAt) {
            this.lastSyncedAt = lastSyncedAt;
            return this;
        }

        public Station build() {return new Station(this);}
    }
}
