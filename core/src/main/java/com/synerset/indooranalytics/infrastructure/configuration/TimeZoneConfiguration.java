package com.synerset.indooranalytics.infrastructure.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
class TimeZoneConfiguration {

    @PostConstruct
    void setApplicationTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
