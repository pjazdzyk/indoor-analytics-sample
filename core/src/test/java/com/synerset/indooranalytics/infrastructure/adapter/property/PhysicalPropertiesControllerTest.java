package com.synerset.indooranalytics.infrastructure.adapter.property;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synerset.hvacengine.fluids.SharedEquations;
import com.synerset.hvacengine.fluids.dryair.DryAirEquations;
import com.synerset.hvacengine.fluids.humidair.HumidAirEquations;
import com.synerset.hvacengine.fluids.humidair.VapourState;
import com.synerset.indooranalyticsapi.property.data.DryAirResponse;
import com.synerset.indooranalyticsapi.property.data.HumidAirResponse;
import com.synerset.unitility.unitsystem.dimensionless.PrandtlNumber;
import com.synerset.unitility.unitsystem.humidity.HumidityRatio;
import com.synerset.unitility.unitsystem.humidity.RelativeHumidity;
import com.synerset.unitility.unitsystem.thermodynamic.*;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withPrecision;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PhysicalPropertiesControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        RestAssured.config = RestAssured.config().objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory((type, s) -> objectMapper));
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @DisplayName("DryAir: should successfully return dry air for given pressure and temperature")
    void getDryAirProperties() {
        // Given
        Pressure expectedPressure = Pressure.ofPascal(100_000);
        Temperature expectedTemperature = Temperature.ofCelsius(25);

        // When
        DryAirResponse dryAirResponse = RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParam("pressure", expectedPressure.toEngineeringFormat())
                .queryParam("temperature", expectedTemperature.toEngineeringFormat())
                .when()
                .get("/indoor-analytics/properties/dry-air")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(DryAirResponse.class);

        DryAirResponse dryAirResponseImperial = RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParam("pressure", expectedPressure.toEngineeringFormat())
                .queryParam("temperature", expectedTemperature.toEngineeringFormat())
                .queryParam("imperial-units", true)
                .when()
                .get("/indoor-analytics/properties/dry-air")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(DryAirResponse.class);

        // Then
        Density expectedDensity = DryAirEquations.density(expectedTemperature, expectedPressure);
        SpecificHeat expectedSpecificHeat = DryAirEquations.specificHeat(expectedTemperature);
        SpecificEnthalpy expectedSpecificEnthalpy = DryAirEquations.specificEnthalpy(expectedTemperature);
        DynamicViscosity expectedDynamicViscosity = DryAirEquations.dynamicViscosity(expectedTemperature);
        KinematicViscosity expectedKinematicViscosity = DryAirEquations.kinematicViscosity(expectedTemperature, expectedPressure);
        ThermalConductivity expectedThermalConductivity = DryAirEquations.thermalConductivity(expectedTemperature);
        PrandtlNumber expectedPrandtlNumber = SharedEquations.prandtlNumber(expectedDynamicViscosity, expectedThermalConductivity, expectedSpecificHeat);

        assertThat(dryAirResponse).isEqualTo(dryAirResponseImperial);

        assertThat(dryAirResponse.pressure()).isEqualTo(expectedPressure);
        assertThat(dryAirResponse.temperature()).isEqualTo(expectedTemperature);
        assertThat(dryAirResponse.density()).isEqualTo(expectedDensity);
        assertThat(dryAirResponse.specificHeat()).isEqualTo(expectedSpecificHeat);
        assertThat(dryAirResponse.specificEnthalpy()).isEqualTo(expectedSpecificEnthalpy);
        assertThat(dryAirResponse.dynamicViscosity()).isEqualTo(expectedDynamicViscosity);
        assertThat(dryAirResponse.kinematicViscosity()).isEqualTo(expectedKinematicViscosity);
        assertThat(dryAirResponse.thermalConductivity()).isEqualTo(expectedThermalConductivity);
        assertThat(dryAirResponse.prandtlNumber()).isEqualTo(expectedPrandtlNumber);

    }

    @Test
    @DisplayName("HumidAir: should successfully return humid air for given pressure and temperature")
    void getHumidAirProperties() {
        // Given
        Pressure expectedPressure = Pressure.ofPascal(100_000);
        Temperature expectedTemperature = Temperature.ofCelsius(25);
        RelativeHumidity expectedRelativeHumidity = RelativeHumidity.ofPercentage(45);

        // When
        HumidAirResponse humidAirResponse = RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParam("pressure", expectedPressure.toEngineeringFormat())
                .queryParam("temperature", expectedTemperature.toEngineeringFormat())
                .queryParam("relative-humidity", expectedRelativeHumidity.toEngineeringFormat())
                .when()
                .get("/indoor-analytics/properties/humid-air")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(HumidAirResponse.class);

        HumidAirResponse humidAirResponseImperial = RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParam("pressure", expectedPressure.toEngineeringFormat())
                .queryParam("temperature", expectedTemperature.toEngineeringFormat())
                .queryParam("relative-humidity", expectedRelativeHumidity.toEngineeringFormat())
                .queryParam("imperial-units", true)
                .when()
                .get("/indoor-analytics/properties/humid-air")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(HumidAirResponse.class);

        // Then
        VapourState expectedVapourState = VapourState.UNSATURATED;
        Pressure expectedSaturationPressure = HumidAirEquations.saturationPressure(expectedTemperature);
        HumidityRatio expectedHumidityRatio = HumidAirEquations.humidityRatio(expectedRelativeHumidity, expectedSaturationPressure, expectedPressure);
        Density expectedDensity = HumidAirEquations.density(expectedTemperature, expectedHumidityRatio, expectedPressure);
        HumidityRatio expectedMaxHumidityRatio = HumidAirEquations.maxHumidityRatio(expectedSaturationPressure, expectedPressure);
        Temperature expectedWetBulbTemperature = HumidAirEquations.wetBulbTemperature(expectedTemperature, expectedRelativeHumidity, expectedPressure);
        Temperature expectedDewPointTemperature = HumidAirEquations.dewPointTemperature(expectedTemperature, expectedRelativeHumidity, expectedPressure);
        SpecificHeat expectedSpecificHeat = HumidAirEquations.specificHeat(expectedTemperature, expectedHumidityRatio);
        SpecificEnthalpy expectedSpecificEnthalpy = HumidAirEquations.specificEnthalpy(expectedTemperature, expectedHumidityRatio, expectedPressure);
        DynamicViscosity expectedDynamicViscosity = HumidAirEquations.dynamicViscosity(expectedTemperature, expectedHumidityRatio);
        KinematicViscosity expectedKinematicViscosity = HumidAirEquations.kinematicViscosity(expectedTemperature, expectedHumidityRatio, expectedDensity);
        ThermalConductivity expectedThermalConductivity = HumidAirEquations.thermalConductivity(expectedTemperature, expectedHumidityRatio);
        ThermalDiffusivity expectedThermalDiffusivity = SharedEquations.thermalDiffusivity(expectedDensity, expectedThermalConductivity, expectedSpecificHeat);
        PrandtlNumber expeectedPrandtlNumber = SharedEquations.prandtlNumber(expectedDynamicViscosity, expectedThermalConductivity, expectedSpecificHeat);

        assertThat(humidAirResponse).isEqualTo(humidAirResponseImperial);

        assertThat(humidAirResponse.vapourState()).isEqualTo(expectedVapourState);
        assertThat(humidAirResponse.saturationPressure()).isEqualTo(expectedSaturationPressure);
        assertThat(humidAirResponse.humidityRatio()).isEqualTo(expectedHumidityRatio);
        assertThat(humidAirResponse.maxHumidityRatio()).isEqualTo(expectedMaxHumidityRatio);
        assertThat(humidAirResponse.density()).isEqualTo(expectedDensity);
        assertThat(humidAirResponse.wetBulbTemperature()).isEqualTo(expectedWetBulbTemperature);
        assertThat(humidAirResponse.dewPointTemperature()).isEqualTo(expectedDewPointTemperature);
        assertThat(humidAirResponse.specificHeat()).isEqualTo(expectedSpecificHeat);
        assertThat(humidAirResponse.specificEnthalpy()).isEqualTo(expectedSpecificEnthalpy);
        assertThat(humidAirResponse.dynamicViscosity()).isEqualTo(expectedDynamicViscosity);
        assertThat(humidAirResponse.kinematicViscosity()).isEqualTo(expectedKinematicViscosity);
        assertThat(humidAirResponse.thermalConductivity()).isEqualTo(expectedThermalConductivity);
        assertThat(humidAirResponse.thermalDiffusivity()).isEqualTo(expectedThermalDiffusivity);
        assertThat(humidAirResponse.prandtlNumber()).isEqualTo(expeectedPrandtlNumber);

    }

    @Test
    @DisplayName("HumidAir: should successfully return humid air derived from wet bulb temperature")
    void getHumidAirPropertiesFromWbt() {
        // Given
        Pressure expectedPressure = Pressure.ofPascal(100_000);
        Temperature expectedWetBulbTemperature = Temperature.ofCelsius(25);
        RelativeHumidity expectedRelativeHumidity = RelativeHumidity.ofPercentage(45);

        // When
        HumidAirResponse humidAirResponse = RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParam("pressure", expectedPressure.toEngineeringFormat())
                .queryParam("wet-bulb-temperature", expectedWetBulbTemperature.toEngineeringFormat())
                .queryParam("relative-humidity", expectedRelativeHumidity.toEngineeringFormat())
                .when()
                .get("/indoor-analytics/properties/humid-air/from-wet-bulb")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(HumidAirResponse.class);

        HumidAirResponse humidAirResponseImperial = RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParam("pressure", expectedPressure.toEngineeringFormat())
                .queryParam("wet-bulb-temperature", expectedWetBulbTemperature.toEngineeringFormat())
                .queryParam("relative-humidity", expectedRelativeHumidity.toEngineeringFormat())
                .queryParam("imperial-units", true)
                .when()
                .get("/indoor-analytics/properties/humid-air/from-wet-bulb")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(HumidAirResponse.class);

        // Then
        VapourState expectedVapourState = VapourState.UNSATURATED;
        Temperature expectedTemperature = HumidAirEquations.dryBulbTemperatureWbtRH(expectedWetBulbTemperature, expectedRelativeHumidity, expectedPressure);
        Pressure expectedSaturationPressure = HumidAirEquations.saturationPressure(expectedTemperature);
        HumidityRatio expectedHumidityRatio = HumidAirEquations.humidityRatio(expectedRelativeHumidity, expectedSaturationPressure, expectedPressure);
        Density expectedDensity = HumidAirEquations.density(expectedTemperature, expectedHumidityRatio, expectedPressure);
        HumidityRatio expectedMaxHumidityRatio = HumidAirEquations.maxHumidityRatio(expectedSaturationPressure, expectedPressure);
        Temperature expectedDewPointTemperature = HumidAirEquations.dewPointTemperature(expectedTemperature, expectedRelativeHumidity, expectedPressure);
        SpecificHeat expectedSpecificHeat = HumidAirEquations.specificHeat(expectedTemperature, expectedHumidityRatio);
        SpecificEnthalpy expectedSpecificEnthalpy = HumidAirEquations.specificEnthalpy(expectedTemperature, expectedHumidityRatio, expectedPressure);
        DynamicViscosity expectedDynamicViscosity = HumidAirEquations.dynamicViscosity(expectedTemperature, expectedHumidityRatio);
        KinematicViscosity expectedKinematicViscosity = HumidAirEquations.kinematicViscosity(expectedTemperature, expectedHumidityRatio, expectedDensity);
        ThermalConductivity expectedThermalConductivity = HumidAirEquations.thermalConductivity(expectedTemperature, expectedHumidityRatio);
        ThermalDiffusivity expectedThermalDiffusivity = SharedEquations.thermalDiffusivity(expectedDensity, expectedThermalConductivity, expectedSpecificHeat);
        PrandtlNumber expeectedPrandtlNumber = SharedEquations.prandtlNumber(expectedDynamicViscosity, expectedThermalConductivity, expectedSpecificHeat);

        assertThat(humidAirResponse).isEqualTo(humidAirResponseImperial);

        assertThat(humidAirResponse.vapourState()).isEqualTo(expectedVapourState);
        assertThat(humidAirResponse.saturationPressure()).isEqualTo(expectedSaturationPressure);
        assertThat(humidAirResponse.humidityRatio()).isEqualTo(expectedHumidityRatio);
        assertThat(humidAirResponse.maxHumidityRatio()).isEqualTo(expectedMaxHumidityRatio);
        assertThat(humidAirResponse.density()).isEqualTo(expectedDensity);
        assertThat(humidAirResponse.wetBulbTemperature()).isEqualTo(expectedWetBulbTemperature);
        assertThat(humidAirResponse.dewPointTemperature()).isEqualTo(expectedDewPointTemperature);
        assertThat(humidAirResponse.specificHeat()).isEqualTo(expectedSpecificHeat);
        assertThat(humidAirResponse.specificEnthalpy()).isEqualTo(expectedSpecificEnthalpy);
        assertThat(humidAirResponse.dynamicViscosity()).isEqualTo(expectedDynamicViscosity);
        assertThat(humidAirResponse.kinematicViscosity()).isEqualTo(expectedKinematicViscosity);
        assertThat(humidAirResponse.thermalConductivity()).isEqualTo(expectedThermalConductivity);
        assertThat(humidAirResponse.thermalDiffusivity()).isEqualTo(expectedThermalDiffusivity);
        assertThat(humidAirResponse.prandtlNumber()).isEqualTo(expeectedPrandtlNumber);

    }

    @Test
    @DisplayName("HumidAir: should successfully return humid air derived from dew point temperature")
    void getHumidAirPropertiesFromTdp() {
        // Given
        Pressure expectedPressure = Pressure.ofPascal(100_000);
        Temperature expectedDewPointTemperature = Temperature.ofCelsius(15);
        RelativeHumidity expectedRelativeHumidity = RelativeHumidity.ofPercentage(45);

        // When
        HumidAirResponse humidAirResponse = RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParam("pressure", expectedPressure.toEngineeringFormat())
                .queryParam("dew-point-temperature", expectedDewPointTemperature.toEngineeringFormat())
                .queryParam("relative-humidity", expectedRelativeHumidity.toEngineeringFormat())
                .when()
                .get("/indoor-analytics/properties/humid-air/from-dew-point")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(HumidAirResponse.class);

        HumidAirResponse humidAirResponseImperial = RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParam("pressure", expectedPressure.toEngineeringFormat())
                .queryParam("dew-point-temperature", expectedDewPointTemperature.toEngineeringFormat())
                .queryParam("relative-humidity", expectedRelativeHumidity.toEngineeringFormat())
                .queryParam("imperial-units", true)
                .when()
                .get("/indoor-analytics/properties/humid-air/from-dew-point")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(HumidAirResponse.class);

        // Then
        VapourState expectedVapourState = VapourState.UNSATURATED;
        Temperature expectedTemperature = HumidAirEquations.dryBulbTemperatureTdpRH(expectedDewPointTemperature, expectedRelativeHumidity, expectedPressure);
        Pressure expectedSaturationPressure = HumidAirEquations.saturationPressure(expectedTemperature);
        HumidityRatio expectedHumidityRatio = HumidAirEquations.humidityRatio(expectedRelativeHumidity, expectedSaturationPressure, expectedPressure);
        Density expectedDensity = HumidAirEquations.density(expectedTemperature, expectedHumidityRatio, expectedPressure);
        Temperature expectedWetBulbTemperature = HumidAirEquations.wetBulbTemperature(expectedTemperature, expectedRelativeHumidity, expectedPressure);
        HumidityRatio expectedMaxHumidityRatio = HumidAirEquations.maxHumidityRatio(expectedSaturationPressure, expectedPressure);
        SpecificHeat expectedSpecificHeat = HumidAirEquations.specificHeat(expectedTemperature, expectedHumidityRatio);
        SpecificEnthalpy expectedSpecificEnthalpy = HumidAirEquations.specificEnthalpy(expectedTemperature, expectedHumidityRatio, expectedPressure);
        DynamicViscosity expectedDynamicViscosity = HumidAirEquations.dynamicViscosity(expectedTemperature, expectedHumidityRatio);
        KinematicViscosity expectedKinematicViscosity = HumidAirEquations.kinematicViscosity(expectedTemperature, expectedHumidityRatio, expectedDensity);
        ThermalConductivity expectedThermalConductivity = HumidAirEquations.thermalConductivity(expectedTemperature, expectedHumidityRatio);
        ThermalDiffusivity expectedThermalDiffusivity = SharedEquations.thermalDiffusivity(expectedDensity, expectedThermalConductivity, expectedSpecificHeat);
        PrandtlNumber expeectedPrandtlNumber = SharedEquations.prandtlNumber(expectedDynamicViscosity, expectedThermalConductivity, expectedSpecificHeat);

        assertThat(humidAirResponse).isEqualTo(humidAirResponseImperial);

        assertThat(humidAirResponse.vapourState()).isEqualTo(expectedVapourState);
        assertThat(humidAirResponse.saturationPressure()).isEqualTo(expectedSaturationPressure);
        assertThat(humidAirResponse.humidityRatio()).isEqualTo(expectedHumidityRatio);
        assertThat(humidAirResponse.maxHumidityRatio()).isEqualTo(expectedMaxHumidityRatio);
        assertThat(humidAirResponse.density()).isEqualTo(expectedDensity);
        assertThat(humidAirResponse.wetBulbTemperature()).isEqualTo(expectedWetBulbTemperature);
        assertThat(humidAirResponse.dewPointTemperature().getValue()).isEqualTo(expectedDewPointTemperature.getValue(), withPrecision(1E-10));
        assertThat(humidAirResponse.specificHeat()).isEqualTo(expectedSpecificHeat);
        assertThat(humidAirResponse.specificEnthalpy()).isEqualTo(expectedSpecificEnthalpy);
        assertThat(humidAirResponse.dynamicViscosity()).isEqualTo(expectedDynamicViscosity);
        assertThat(humidAirResponse.kinematicViscosity()).isEqualTo(expectedKinematicViscosity);
        assertThat(humidAirResponse.thermalConductivity()).isEqualTo(expectedThermalConductivity);
        assertThat(humidAirResponse.thermalDiffusivity()).isEqualTo(expectedThermalDiffusivity);
        assertThat(humidAirResponse.prandtlNumber()).isEqualTo(expeectedPrandtlNumber);

    }

    @Test
    @DisplayName("HumidAir: should successfully return humid air derived from specific enthalpy")
    void getHumidAirPropertiesFromIx() {
        // Given
        Pressure expectedPressure = Pressure.ofPascal(100_000);
        SpecificEnthalpy expectedSpecificEnthalpy = SpecificEnthalpy.ofKiloJoulePerKiloGram(12.5);
        HumidityRatio expectedHumidityRatio = HumidityRatio.ofKilogramPerKilogram(0.002);

        // When
        HumidAirResponse humidAirResponse = RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParam("pressure", expectedPressure.toEngineeringFormat())
                .queryParam("specific-enthalpy", expectedSpecificEnthalpy.toEngineeringFormat())
                .queryParam("humidity-ratio", expectedHumidityRatio.toEngineeringFormat())
                .when()
                .get("/indoor-analytics/properties/humid-air/from-enthalpy")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(HumidAirResponse.class);

        HumidAirResponse humidAirResponseImperial = RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParam("pressure", expectedPressure.toEngineeringFormat())
                .queryParam("specific-enthalpy", expectedSpecificEnthalpy.toEngineeringFormat())
                .queryParam("humidity-ratio", expectedHumidityRatio.toEngineeringFormat())
                .queryParam("imperial-units", true)
                .when()
                .get("/indoor-analytics/properties/humid-air/from-enthalpy")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(HumidAirResponse.class);

        // Then
        VapourState expectedVapourState = VapourState.UNSATURATED;
        Temperature expectedTemperature = HumidAirEquations.dryBulbTemperatureIX(expectedSpecificEnthalpy, expectedHumidityRatio, expectedPressure);
        Pressure expectedSaturationPressure = HumidAirEquations.saturationPressure(expectedTemperature);
        RelativeHumidity expectedRelativeHumidity = HumidAirEquations.relativeHumidity(expectedTemperature, expectedHumidityRatio, expectedPressure);
        Density expectedDensity = HumidAirEquations.density(expectedTemperature, expectedHumidityRatio, expectedPressure);
        HumidityRatio expectedMaxHumidityRatio = HumidAirEquations.maxHumidityRatio(expectedSaturationPressure, expectedPressure);
        Temperature expectedWetBulbTemperature = HumidAirEquations.wetBulbTemperature(expectedTemperature, expectedRelativeHumidity, expectedPressure);
        Temperature expectedDewPointTemperature = HumidAirEquations.dewPointTemperature(expectedTemperature, expectedRelativeHumidity, expectedPressure);
        SpecificHeat expectedSpecificHeat = HumidAirEquations.specificHeat(expectedTemperature, expectedHumidityRatio);
        DynamicViscosity expectedDynamicViscosity = HumidAirEquations.dynamicViscosity(expectedTemperature, expectedHumidityRatio);
        KinematicViscosity expectedKinematicViscosity = HumidAirEquations.kinematicViscosity(expectedTemperature, expectedHumidityRatio, expectedDensity);
        ThermalConductivity expectedThermalConductivity = HumidAirEquations.thermalConductivity(expectedTemperature, expectedHumidityRatio);
        ThermalDiffusivity expectedThermalDiffusivity = SharedEquations.thermalDiffusivity(expectedDensity, expectedThermalConductivity, expectedSpecificHeat);
        PrandtlNumber expeectedPrandtlNumber = SharedEquations.prandtlNumber(expectedDynamicViscosity, expectedThermalConductivity, expectedSpecificHeat);

        assertThat(humidAirResponse).isEqualTo(humidAirResponseImperial);

        assertThat(humidAirResponse.vapourState()).isEqualTo(expectedVapourState);
        assertThat(humidAirResponse.saturationPressure()).isEqualTo(expectedSaturationPressure);
        assertThat(humidAirResponse.humidityRatio()).isEqualTo(expectedHumidityRatio);
        assertThat(humidAirResponse.maxHumidityRatio()).isEqualTo(expectedMaxHumidityRatio);
        assertThat(humidAirResponse.density()).isEqualTo(expectedDensity);
        assertThat(humidAirResponse.wetBulbTemperature()).isEqualTo(expectedWetBulbTemperature);
        assertThat(humidAirResponse.dewPointTemperature()).isEqualTo(expectedDewPointTemperature);
        assertThat(humidAirResponse.specificHeat()).isEqualTo(expectedSpecificHeat);
        assertThat(humidAirResponse.specificEnthalpy().getValue()).isEqualTo(expectedSpecificEnthalpy.getValue(), withPrecision(1E-13));
        assertThat(humidAirResponse.dynamicViscosity()).isEqualTo(expectedDynamicViscosity);
        assertThat(humidAirResponse.kinematicViscosity()).isEqualTo(expectedKinematicViscosity);
        assertThat(humidAirResponse.thermalConductivity()).isEqualTo(expectedThermalConductivity);
        assertThat(humidAirResponse.thermalDiffusivity()).isEqualTo(expectedThermalDiffusivity);
        assertThat(humidAirResponse.prandtlNumber()).isEqualTo(expeectedPrandtlNumber);
    }

    @Test
    @DisplayName("HumidAir: should successfully return humid air derived from humidity")
    void getHumidAirPropertiesFromXRh() {
        // Given
        Pressure expectedPressure = Pressure.ofPascal(100_000);
        HumidityRatio expectedHumidityRatio = HumidityRatio.ofKilogramPerKilogram(0.002);
        RelativeHumidity expectedRelativeHumidity = RelativeHumidity.ofPercentage(45.5);

        // When
        HumidAirResponse humidAirResponse = RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParam("pressure", expectedPressure.toEngineeringFormat())
                .queryParam("humidity-ratio", expectedHumidityRatio.toEngineeringFormat())
                .queryParam("relative-humidity", expectedRelativeHumidity.toEngineeringFormat())
                .when()
                .get("/indoor-analytics/properties/humid-air/from-humidity")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(HumidAirResponse.class);

        HumidAirResponse humidAirResponseImperial = RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParam("pressure", expectedPressure.toEngineeringFormat())
                .queryParam("humidity-ratio", expectedHumidityRatio.toEngineeringFormat())
                .queryParam("relative-humidity", expectedRelativeHumidity.toEngineeringFormat())
                .queryParam("imperial-units", true)
                .when()
                .get("/indoor-analytics/properties/humid-air/from-humidity")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(HumidAirResponse.class);

        // Then
        VapourState expectedVapourState = VapourState.UNSATURATED;
        Temperature expectedTemperature = HumidAirEquations.dryBulbTemperatureXRH(expectedHumidityRatio, expectedRelativeHumidity, expectedPressure);
        Pressure expectedSaturationPressure = HumidAirEquations.saturationPressure(expectedTemperature);
        Density expectedDensity = HumidAirEquations.density(expectedTemperature, expectedHumidityRatio, expectedPressure);
        HumidityRatio expectedMaxHumidityRatio = HumidAirEquations.maxHumidityRatio(expectedSaturationPressure, expectedPressure);
        Temperature expectedWetBulbTemperature = HumidAirEquations.wetBulbTemperature(expectedTemperature, expectedRelativeHumidity, expectedPressure);
        Temperature expectedDewPointTemperature = HumidAirEquations.dewPointTemperature(expectedTemperature, expectedRelativeHumidity, expectedPressure);
        SpecificHeat expectedSpecificHeat = HumidAirEquations.specificHeat(expectedTemperature, expectedHumidityRatio);
        SpecificEnthalpy expectedSpecificEnthalpy = HumidAirEquations.specificEnthalpy(expectedTemperature, expectedHumidityRatio, expectedPressure);
        DynamicViscosity expectedDynamicViscosity = HumidAirEquations.dynamicViscosity(expectedTemperature, expectedHumidityRatio);
        KinematicViscosity expectedKinematicViscosity = HumidAirEquations.kinematicViscosity(expectedTemperature, expectedHumidityRatio, expectedDensity);
        ThermalConductivity expectedThermalConductivity = HumidAirEquations.thermalConductivity(expectedTemperature, expectedHumidityRatio);
        ThermalDiffusivity expectedThermalDiffusivity = SharedEquations.thermalDiffusivity(expectedDensity, expectedThermalConductivity, expectedSpecificHeat);
        PrandtlNumber expeectedPrandtlNumber = SharedEquations.prandtlNumber(expectedDynamicViscosity, expectedThermalConductivity, expectedSpecificHeat);

        assertThat(humidAirResponse).isEqualTo(humidAirResponseImperial);

        assertThat(humidAirResponse.vapourState()).isEqualTo(expectedVapourState);
        assertThat(humidAirResponse.saturationPressure()).isEqualTo(expectedSaturationPressure);
        assertThat(humidAirResponse.humidityRatio()).isEqualTo(expectedHumidityRatio);
        assertThat(humidAirResponse.maxHumidityRatio()).isEqualTo(expectedMaxHumidityRatio);
        assertThat(humidAirResponse.density()).isEqualTo(expectedDensity);
        assertThat(humidAirResponse.wetBulbTemperature()).isEqualTo(expectedWetBulbTemperature);
        assertThat(humidAirResponse.dewPointTemperature()).isEqualTo(expectedDewPointTemperature);
        assertThat(humidAirResponse.specificHeat()).isEqualTo(expectedSpecificHeat);
        assertThat(humidAirResponse.specificEnthalpy()).isEqualTo(expectedSpecificEnthalpy);
        assertThat(humidAirResponse.dynamicViscosity()).isEqualTo(expectedDynamicViscosity);
        assertThat(humidAirResponse.kinematicViscosity()).isEqualTo(expectedKinematicViscosity);
        assertThat(humidAirResponse.thermalConductivity()).isEqualTo(expectedThermalConductivity);
        assertThat(humidAirResponse.thermalDiffusivity()).isEqualTo(expectedThermalDiffusivity);
        assertThat(humidAirResponse.prandtlNumber()).isEqualTo(expeectedPrandtlNumber);
    }

}