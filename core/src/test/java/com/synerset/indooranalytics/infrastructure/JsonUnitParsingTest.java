package com.synerset.indooranalytics.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synerset.unitility.unitsystem.common.*;
import com.synerset.unitility.unitsystem.dimensionless.BypassFactor;
import com.synerset.unitility.unitsystem.dimensionless.GrashofNumber;
import com.synerset.unitility.unitsystem.dimensionless.PrandtlNumber;
import com.synerset.unitility.unitsystem.dimensionless.ReynoldsNumber;
import com.synerset.unitility.unitsystem.flow.MassFlow;
import com.synerset.unitility.unitsystem.flow.VolumetricFlow;
import com.synerset.unitility.unitsystem.humidity.HumidityRatio;
import com.synerset.unitility.unitsystem.humidity.RelativeHumidity;
import com.synerset.unitility.unitsystem.mechanical.Force;
import com.synerset.unitility.unitsystem.mechanical.Momentum;
import com.synerset.unitility.unitsystem.mechanical.Torque;
import com.synerset.unitility.unitsystem.thermodynamic.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JsonUnitParsingTest {

    private static final double TEST_VALUE = 1.5;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Serialization / Deserialization: should successfully serialize and deserialize physical quantity types")
    void shouldSerializeAndDeserializeCorrectlyAllUnitilityQuantities() throws JsonProcessingException {
        // Given
        TestUnitsRecord expectedUnitRecord = new TestUnitsRecord(
                Angle.ofRadians(TEST_VALUE),
                Area.ofSquareMeters(TEST_VALUE),
                Distance.ofMiles(TEST_VALUE),
                Mass.ofGrams(TEST_VALUE),
                Velocity.ofMetersPerSecond(TEST_VALUE),
                Volume.ofCubicMeters(TEST_VALUE),
                BypassFactor.of(TEST_VALUE),
                GrashofNumber.of(TEST_VALUE),
                PrandtlNumber.of(TEST_VALUE),
                ReynoldsNumber.of(TEST_VALUE),
                MassFlow.ofKilogramsPerSecond(TEST_VALUE),
                VolumetricFlow.ofGallonsPerHour(TEST_VALUE),
                HumidityRatio.ofKilogramPerKilogram(TEST_VALUE),
                RelativeHumidity.ofDecimal(TEST_VALUE),
                Force.ofDynes(TEST_VALUE),
                Momentum.ofGramCentimetrePerSecond(TEST_VALUE),
                Torque.ofNewtonMeters(TEST_VALUE),
                Density.ofKilogramPerCubicMeter(TEST_VALUE),
                DynamicViscosity.ofKiloGramPerMeterSecond(TEST_VALUE),
                Energy.ofKiloCalorie(TEST_VALUE),
                KinematicViscosity.ofSquareFootPerSecond(TEST_VALUE),
                Power.ofHorsePower(TEST_VALUE),
                Pressure.ofBar(TEST_VALUE),
                SpecificEnthalpy.ofBTUPerPound(TEST_VALUE),
                SpecificHeat.ofKiloJoulePerKiloGramKelvin(TEST_VALUE),
                Temperature.ofFahrenheit(TEST_VALUE),
                ThermalConductivity.ofWattsPerMeterKelvin(TEST_VALUE),
                ThermalDiffusivity.ofSquareFeetPerSecond(TEST_VALUE)
                );
        // When
        String unitsAsJson = objectMapper.writeValueAsString(expectedUnitRecord);
        TestUnitsRecord actualUnitRecord = objectMapper.readValue(unitsAsJson, TestUnitsRecord.class);

        // Then
        assertThat(actualUnitRecord).isEqualTo(expectedUnitRecord);
    }

}