package com.fueltracker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "scheduler")
@Getter @Setter
public class SchedulerProperties {
    private List<String> stationIds;
}
