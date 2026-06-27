package com.fueltracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class Tracker2Application {

    public static void main(String[] args) {
        SpringApplication.run(Tracker2Application.class, args);
    }

}
