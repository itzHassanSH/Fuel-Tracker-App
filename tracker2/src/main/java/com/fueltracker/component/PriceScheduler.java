package com.fueltracker.component;

import com.fueltracker.station.service.StationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PriceScheduler {
    private final StationService stationService;

    public PriceScheduler(StationService service) {
        this.stationService = service;
    }

    @Scheduled(cron = "${scheduler.dense-first-half}")
    public void schedulePricesFirstHalf() {
        stationService.schedulePrice();
    }

    @Scheduled(cron = "${scheduler.dense-second-half}")
    public void schedulePricesSecondHalf() {
        stationService.schedulePrice();
    }

    @Scheduled(cron = "${scheduler.sparse}")
    public void schedulePricesRest() {
        stationService.schedulePrice();
    }
}
