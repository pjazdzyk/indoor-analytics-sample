package com.synerset.indooranalyticsapi.process.heating.data;

import com.synerset.indooranalyticsapi.process.common.OutletAirFlowResponse;
import com.synerset.unitility.unitsystem.thermodynamic.Power;
import io.swagger.v3.oas.annotations.media.Schema;

public record HeatingProcessResponse(
        @Schema(example = "HeatingFromTemperature")
        String heatingStrategy,
        @Schema(example = "{\"value\": 524688.965,\"unit\": \"W\"}")
        Power heatingPower,
        OutletAirFlowResponse outletAirFlow
) {

    public HeatingProcessResponse toImperialUnits() {
        return new HeatingProcessResponse(
                heatingStrategy,
                heatingPower.toBTUPerHour(),
                outletAirFlow.toImperialUnits()
        );
    }
}