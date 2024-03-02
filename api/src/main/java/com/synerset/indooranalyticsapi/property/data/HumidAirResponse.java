package com.synerset.indooranalyticsapi.property.data;

import com.synerset.hvacengine.fluids.humidair.VapourState;
import com.synerset.unitility.unitsystem.dimensionless.PrandtlNumber;
import com.synerset.unitility.unitsystem.humidity.HumidityRatio;
import com.synerset.unitility.unitsystem.humidity.RelativeHumidity;
import com.synerset.unitility.unitsystem.thermodynamic.*;
import io.swagger.v3.oas.annotations.media.Schema;

public record HumidAirResponse(
        @Schema(example = "SATURATED")
        VapourState vapourState,
        @Schema(example = "{\"value\": -20.0,\"unit\": \"°C\"}")
        Temperature temperature,
        @Schema(example = "{\"value\": 101325.0,\"unit\": \"Pa\"}")
        Pressure pressure,
        @Schema(example = "{\"value\": 1.391,\"unit\": \"kg/m³\"}")
        Density density,
        @Schema(example = "{\"value\": 31.543,\"unit\": \"%\"}")
        RelativeHumidity relativeHumidity,
        @Schema(example = "{\"value\": 103.260,\"unit\": \"Pa\"}")
        Pressure saturationPressure,
        @Schema(example = "{\"value\": 2.0E-4,\"unit\": \"kg/kg\"}")
        HumidityRatio humidityRatio,
        @Schema(example = "{\"value\": 3.34E-4,\"unit\": \"kg/kg\"}")
        HumidityRatio maxHumidityRatio,
        @Schema(example = "{\"value\": -21.054,\"unit\": \"°C\"}")
        Temperature wetBulbTemperature,
        @Schema(example = "{\"value\": -31.476,\"unit\": \"°C\"}")
        Temperature dewPointTemperature,
        @Schema(example = "{\"value\": 1.004,\"unit\": \"kJ/(kg·K)\"}")
        SpecificHeat specificHeat,
        @Schema(example = "{\"value\": -19.567,\"unit\": \"kJ/kg\"}")
        SpecificEnthalpy specificEnthalpy,
        @Schema(example = "{\"value\": 1.6078,\"unit\": \"kg/(m·s)\"}")
        DynamicViscosity dynamicViscosity,
        @Schema(example = "{\"value\": 1.1536,\"unit\": \"m²/s\"}")
        KinematicViscosity kinematicViscosity,
        @Schema(example = "{\"value\": 0.0228,\"unit\": \"W/(m·K)\"}")
        ThermalConductivity thermalConductivity,
        @Schema(example = "{\"value\": 1.6306,\"unit\": \"m²/s\"}")
        ThermalDiffusivity thermalDiffusivity,
        @Schema(example = "{\"value\": 0.7074}")
        PrandtlNumber prandtlNumber
) {
    public HumidAirResponse toImperialUnits() {
        return new HumidAirResponse(
                vapourState,
                temperature.toFahrenheit(),
                pressure.toPsi(),
                density.toPoundPerCubicFoot(),
                relativeHumidity,
                saturationPressure.toPsi(),
                humidityRatio.toPoundPerPound(),
                maxHumidityRatio.toPoundPerPound(),
                wetBulbTemperature().toFahrenheit(),
                dewPointTemperature.toFahrenheit(),
                specificHeat.toBTUPerPoundFahrenheit(),
                specificEnthalpy.toBTUPerPound(),
                dynamicViscosity.toPoise(),
                kinematicViscosity.toSquareFootPerSecond(),
                thermalConductivity.toBTUPerHourFeetFahrenheit(),
                thermalDiffusivity.toSquareFeetPerSecond(),
                prandtlNumber
        );
    }
}
