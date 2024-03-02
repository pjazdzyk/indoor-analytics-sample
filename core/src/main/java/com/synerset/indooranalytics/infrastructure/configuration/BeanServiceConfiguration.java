package com.synerset.indooranalytics.infrastructure.configuration;

import com.synerset.indooranalytics.domain.process.HeatingProcessPort;
import com.synerset.indooranalytics.domain.property.PhysicalPropertiesPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class BeanServiceConfiguration {

    @Bean
    PhysicalPropertiesPort createPhysicalPropertiesService() {
        return PhysicalPropertiesPort.create();
    }

    @Bean
    HeatingProcessPort createHeatingProcessService() {
        return HeatingProcessPort.create();
    }

}