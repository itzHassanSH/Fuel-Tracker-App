package com.fueltracker.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Schedule program to fetch prices for favourited Stations every ... unit time.
 * Create PriceSnapShots for these fetched prices and save into Repo.
 * Thus allows a history for price for a certain station (i.e. one that is favourited) to be built.
 * Later perform analysis on this data
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {
    // Unlike a cache manager, you never instantiate the scheduler yourself in the common case.
}
