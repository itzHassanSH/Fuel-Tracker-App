package com.fueltracker.station;

import com.fueltracker.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class FavouriteStation {
    @Id
    @GeneratedValue
    private Long id;

    // A user can naturally have many favourite stations
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // A station can be favourited by many users
    // Many different favorite station objects can still belong to same station
    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    private LocalDateTime createdAt;

    public FavouriteStation() {};
    private FavouriteStation(Builder builder) {
        this.user = builder.user;
        this.station = builder.station;
        this.createdAt = builder.createdAt;
    }

    public static class Builder {
        private User user;
        private Station station;
        private LocalDateTime createdAt;

        public Builder user(User user) {
            this.user = user;
            return this;
        }
        public Builder station(Station station) {
            this.station = station;
            return this;
        }
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public FavouriteStation build() {return new FavouriteStation(this);}
    }
}
