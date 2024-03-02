package com.synerset.indooranalyticsapi.property.data;

import com.synerset.unitility.unitsystem.dimensionless.PrandtlNumber;
import com.synerset.unitility.unitsystem.thermodynamic.*;
import io.swagger.v3.oas.annotations.media.Schema;

public record DryAirResponse(
        @Schema(example = "{\"value\": -20.0,\"unit\": \"°C\"}")
        Temperature temperature,
        @Schema(example = "{\"value\": 101325.0,\"unit\": \"Pa\"}")
        Pressure pressure,
        @Schema(example = "{\"value\": 1.394,\"unit\": \"kg/m³\"}")
        Density density,
        @Schema(example = "{\"value\": 1.003,\"unit\": \"kJ/(kg·K)\"}")
        SpecificHeat specificHeat,
        @Schema(example = "{\"value\": -20.6,\"unit\": \"kJ/kg\"}")
        SpecificEnthalpy specificEnthalpy,
        @Schema(example = "{\"value\": 1.6000,\"unit\": \"kg/(m·s)\"}")
        DynamicViscosity dynamicViscosity,
        @Schema(example = "{\"value\": 1.153,\"unit\": \"m²/s\"}")
        KinematicViscosity kinematicViscosity,
        @Schema(example = "{\"value\": 0.0227,\"unit\": \"W/(m·K)\"}")
        ThermalConductivity thermalConductivity,
        @Schema(example = "{\"value\": 0.7074}")
        PrandtlNumber prandtlNumber
) {

    public DryAirResponse toImperialUnits() {
        return new DryAirResponse(
                temperature.toFahrenheit(),
                pressure.toPsi(),
                density.toPoundPerCubicFoot(),
                specificHeat.toBTUPerPoundFahrenheit(),
                specificEnthalpy.toBTUPerPound(),
                dynamicViscosity.toPoise(),
                kinematicViscosity.toSquareFootPerSecond(),
                thermalConductivity.toBTUPerHourFeetFahrenheit(),
                prandtlNumber
        );
    }

}