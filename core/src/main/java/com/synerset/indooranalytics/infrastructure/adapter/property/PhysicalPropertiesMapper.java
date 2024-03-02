package com.synerset.indooranalytics.infrastructure.adapter.property;

import com.synerset.hvacengine.fluids.dryair.DryAir;
import com.synerset.hvacengine.fluids.humidair.HumidAir;
import com.synerset.indooranalyticsapi.property.data.DryAirResponse;
import com.synerset.indooranalyticsapi.property.data.HumidAirResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
interface PhysicalPropertiesMapper {

    DryAirResponse toDryAirResponse(DryAir dryAir);

    HumidAirResponse toHumidAirResponse(HumidAir humidAir);

}