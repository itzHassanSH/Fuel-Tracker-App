package com.fueltracker.user;

import com.fueltracker.station.FavouriteStation;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long userId;

    private String username;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<FavouriteStation> favouriteStationList;

    private User(Builder builder) {
        this.username = builder.username;
        this.favouriteStationList = builder.favouriteStationList;
    }
    public User() {}

    public static class Builder {
        private List<FavouriteStation> favouriteStationList;
        private String username;

        public Builder favouriteStationList(List<FavouriteStation> favouriteStationList) {
            this.favouriteStationList = favouriteStationList;
            return this;
        }
        public Builder username(String username) {
            this.username = username;
            return this;
        }
        public User build() {return new User(this);}
    }
}
