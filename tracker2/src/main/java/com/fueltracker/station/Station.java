package com.fueltracker.station;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
public class Station {
    @Id
    private String id;

    private String name;
    private String brand;
    private String street;
    private int postalCode;
    private String houseNumber;
    private String place;   // E.g. Stuttgart

    // Coordinates
    private double lat;
    private double lng;

    private double distance;

    private boolean isActive;
    private LocalDateTime lastSyncedAt;

    // both opening times and fuels should lie inside cache and returned after requests -
    // just not saved inside the DB itself

    // here no cascading since deleting a favorite should NOT delete the station itself
    @OneToMany(mappedBy = "station")
    private List<FavouriteStation> favouritedBy;

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
        this.distance = builder.distance;
        this.favouritedBy = builder.favouritedBy;

        this.isActive = builder.isActive;
        this.lastSyncedAt = builder.lastSyncedAt;
    }

    public static class Builder {
        private String id;
        private String name;
        private String brand;
        private String street;
        private int postalCode;
        private String houseNumber;
        private String place;   // E.g. Stuttgart
        private double lat;
        private double lng;
        private double distance;
        private List<FavouriteStation> favouritedBy;
        private boolean isActive;
        private LocalDateTime lastSyncedAt;

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
        public Builder postalCode (int postalCode) {
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
        public Builder distance (double distance) {
            this.distance = distance;
            return this;
        }
        public Builder favouritedBy (List<FavouriteStation> favouritedBy) {
            this.favouritedBy = favouritedBy;
            return this;
        }
        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }
        public Builder lastSyncedAt(LocalDateTime lastSyncedAt) {
            this.lastSyncedAt = lastSyncedAt;
            return this;
        }

        public Station build() {return new Station(this);}
    }
}
