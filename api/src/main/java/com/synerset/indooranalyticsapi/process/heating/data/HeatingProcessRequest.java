package com.synerset.indooranalyticsapi.process.heating.data;

import com.synerset.indooranalyticsapi.process.common.InletAirFlowRequest;
import com.synerset.unitility.unitsystem.humidity.RelativeHumidity;
import com.synerset.unitility.unitsystem.thermodynamic.Power;
import com.synerset.unitility.unitsystem.thermodynamic.Temperature;
import com.synerset.unitility.validation.PhysicalRange;
import jakarta.validation.constraints.NotNull;

public record HeatingProcessRequest(
        @NotNull
        InletAirFlowRequest inletAirFlow,
        @PhysicalRange(min = "0.0kW", max = "300MW")
        Power inputHeatingPower,
        @PhysicalRange(min = "-120oC", max = "165c")
        Temperature targetTemperature,
        @PhysicalRange(min = "0.5%", max = "100%")
        RelativeHumidity targetRelativeHumidity
) {}