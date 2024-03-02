package com.synerset.indooranalyticsapi.process.common;

import com.synerset.unitility.unitsystem.flow.MassFlow;
import com.synerset.unitility.unitsystem.flow.VolumetricFlow;
import com.synerset.unitility.unitsystem.humidity.HumidityRatio;
import com.synerset.unitility.unitsystem.humidity.RelativeHumidity;
import com.synerset.unitility.unitsystem.thermodynamic.Pressure;
import com.synerset.unitility.unitsystem.thermodynamic.SpecificEnthalpy;
import com.synerset.unitility.unitsystem.thermodynamic.Temperature;
import io.swagger.v3.oas.annotations.media.Schema;

public record OutletAirFlowResponse(
        @Schema(example = "UNSATURATED")
        String outletVapourState,
        @Schema(example = "{\"value\": 101325.0,\"unit\": \"Pa\"}")
        Pressure outletPressure,
        @Schema(example = "{\"value\": 25.0,\"unit\": \"oC\"}")
        Temperature outletTemperature,
        @Schema(example = "{\"value\": 3.19,\"unit\": \"%\"}")
        RelativeHumidity outletRelativeHumidity,
        @Schema(example = "{\"value\": 6.217E-4,\"unit\": \"kg/kg\"}")
        HumidityRatio outletHumidityRatio,
        @Schema(example = "{\"value\": 26.707,\"unit\": \"kJ/kg\"}")
        SpecificEnthalpy outletSpecificEnthalpy,
        @Schema(example = "{\"value\": 11.60,\"unit\": \"kg/s\"}")
        MassFlow outletMassFlow,
        @Schema(example = "{\"value\": 11.59,\"unit\": \"kg/s\"}")
        MassFlow outletDryAirMassFlow,
        @Schema(example = "{\"value\": 35332.806,\"unit\": \"m3/h\"}")
        VolumetricFlow outletVolFlow
) {

    public OutletAirFlowResponse toImperialUnits() {
        return new OutletAirFlowResponse(
                outletVapourState,
                outletPressure.toPsi(),
                outletTemperature.toFahrenheit(),
                outletRelativeHumidity,
                outletHumidityRatio.toPoundPerPound(),
                outletSpecificEnthalpy.toBTUPerPound(),
                outletMassFlow.toPoundsPerSecond(),
                outletDryAirMassFlow.toPoundsPerSecond(),
                outletVolFlow.toCubicFeetPerMinute()
        );
    }

}