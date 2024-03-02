package com.synerset.indooranalytics.infrastructure.adapter.process;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synerset.hvacengine.fluids.humidair.FlowOfHumidAir;
import com.synerset.indooranalyticsapi.process.common.InletAirFlowRequest;
import com.synerset.indooranalyticsapi.process.common.OutletAirFlowResponse;
import com.synerset.indooranalyticsapi.process.heating.data.HeatingProcessRequest;
import com.synerset.indooranalyticsapi.process.heating.data.HeatingProcessResponse;
import com.synerset.unitility.unitsystem.humidity.HumidityRatio;
import com.synerset.unitility.unitsystem.humidity.RelativeHumidity;
import com.synerset.unitility.unitsystem.thermodynamic.Power;
import com.synerset.unitility.unitsystem.thermodynamic.Pressure;
import com.synerset.unitility.unitsystem.thermodynamic.Temperature;
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
class HeatingProcessControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProcessControllerMapper processMapper;

    @BeforeEach
    void init() {
        RestAssured.config = RestAssured.config().objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory((type, s) -> objectMapper));
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @DisplayName("Heating: should successfully return heating results for input power")
    void getHeatingForInputPower() {
        // Given
        FlowOfHumidAir flowOfHumidAir = FlowOfHumidAir.ofValues(-20, 95, 20_000);
        Power inletPower = Power.ofKiloWatts(50);
        Pressure inletPressure = flowOfHumidAir.getPressure();
        HumidityRatio inletHumidityRatio = flowOfHumidAir.getHumidityRatio();

        // When
        InletAirFlowRequest inletFlowRequest = processMapper.toInletFlowRequest(flowOfHumidAir);

        HeatingProcessRequest heatingProcessRequest = new HeatingProcessRequest(
                inletFlowRequest,
                inletPower,
                null,
                null
        );

        HeatingProcessResponse heatingProcessResponse = RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(heatingProcessRequest)
                .when()
                .post("/indoor-analytics/processes/heating/input-power")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(HeatingProcessResponse.class);

        HeatingProcessResponse heatingProcessResponseImperial = RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(heatingProcessRequest)
                .queryParam("imperial-units", true)
                .when()
                .post("/indoor-analytics/processes/heating/input-power")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(HeatingProcessResponse.class);

        // Then
        assertThat(heatingProcessResponse).isNotNull();
        assertThat(heatingProcessResponseImperial).isNotNull();
        assertThat(heatingProcessResponse).isEqualTo(heatingProcessResponseImperial);

        OutletAirFlowResponse outletAirFlowResponse = heatingProcessResponse.outletAirFlow();

        assertThat(heatingProcessResponse.heatingStrategy()).isEqualTo("HeatingFromPower");
        assertThat(heatingProcessResponse.heatingPower()).isEqualTo(inletPower);

        assertThat(outletAirFlowResponse.outletPressure()).isEqualTo(inletPressure);
        assertThat(outletAirFlowResponse.outletHumidityRatio()).isEqualTo(inletHumidityRatio);
        assertThat(outletAirFlowResponse.outletMassFlow()).isEqualTo(flowOfHumidAir.getMassFlow());
        assertThat(outletAirFlowResponse.outletDryAirMassFlow()).isEqualTo(flowOfHumidAir.getDryAirMassFlow());

        assertThat(outletAirFlowResponse.outletTemperature().getValue()).isEqualTo(-13.56, withPrecision(0.01));
        assertThat(outletAirFlowResponse.outletRelativeHumidity().getValue()).isEqualTo(52.00, withPrecision(0.01));
        assertThat(outletAirFlowResponse.outletSpecificEnthalpy().getValue()).isEqualTo(-12.11, withPrecision(0.01));

    }

    @Test
    @DisplayName("Heating: should successfully return heating results for target temperature")
    void getHeatingForTargetTemperature() {
        // Given
        FlowOfHumidAir flowOfHumidAir = FlowOfHumidAir.ofValues(-20, 95, 20_000);
        Temperature targetTemperature = Temperature.ofCelsius(15);

        Pressure inletPressure = flowOfHumidAir.getPressure();
        HumidityRatio inletHumidityRatio = flowOfHumidAir.getHumidityRatio();

        // When
        InletAirFlowRequest inletFlowRequest = processMapper.toInletFlowRequest(flowOfHumidAir);

        HeatingProcessRequest heatingProcessRequest = new HeatingProcessRequest(
                inletFlowRequest,
                null,
                targetTemperature,
                null
        );

        HeatingProcessResponse heatingProcessResponse = RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(heatingProcessRequest)
                .when()
                .post("/indoor-analytics/processes/heating/target-temperature")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(HeatingProcessResponse.class);

        HeatingProcessResponse heatingProcessResponseImperial = RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(heatingProcessRequest)
                .queryParam("imperial-units", true)
                .when()
                .post("/indoor-analytics/processes/heating/target-temperature")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(HeatingProcessResponse.class);

        // Then
        assertThat(heatingProcessResponse).isNotNull();
        assertThat(heatingProcessResponseImperial).isNotNull();
        assertThat(heatingProcessResponse).isEqualTo(heatingProcessResponseImperial);

        OutletAirFlowResponse outletAirFlowResponse = heatingProcessResponse.outletAirFlow();

        assertThat(heatingProcessResponse.heatingStrategy()).isEqualTo("HeatingFromTemperature");
        assertThat(heatingProcessResponse.heatingPower().getValue()).isEqualTo(271936.98, withPrecision(0.01));


        assertThat(outletAirFlowResponse.outletPressure()).isEqualTo(inletPressure);
        assertThat(outletAirFlowResponse.outletTemperature()).isEqualTo(targetTemperature);
        assertThat(outletAirFlowResponse.outletHumidityRatio()).isEqualTo(inletHumidityRatio);
        assertThat(outletAirFlowResponse.outletMassFlow()).isEqualTo(flowOfHumidAir.getMassFlow());
        assertThat(outletAirFlowResponse.outletDryAirMassFlow()).isEqualTo(flowOfHumidAir.getDryAirMassFlow());

        assertThat(outletAirFlowResponse.outletRelativeHumidity().getValue()).isEqualTo(5.75, withPrecision(0.01));
        assertThat(outletAirFlowResponse.outletSpecificEnthalpy().getValue()).isEqualTo(16.59, withPrecision(0.01));

    }

    @Test
    @DisplayName("Heating: should successfully return heating results for target relative humidity")
    void getHeatingForTargetRelativeHumidity() {
        // Given
        FlowOfHumidAir flowOfHumidAir = FlowOfHumidAir.ofValues(-20, 95, 20_000);
        RelativeHumidity targetRelativeHumidity = RelativeHumidity.ofPercentage(15);
        Pressure inletPressure = flowOfHumidAir.getPressure();
        HumidityRatio inletHumidityRatio = flowOfHumidAir.getHumidityRatio();

        // When
        InletAirFlowRequest inletFlowRequest = processMapper.toInletFlowRequest(flowOfHumidAir);

        HeatingProcessRequest heatingProcessRequest = new HeatingProcessRequest(
                inletFlowRequest,
                null,
                null,
                targetRelativeHumidity
        );

        HeatingProcessResponse heatingProcessResponse = RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(heatingProcessRequest)
                .when()
                .post("/indoor-analytics/processes/heating/target-relative-humidity")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(HeatingProcessResponse.class);

        HeatingProcessResponse heatingProcessResponseImperial = RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(heatingProcessRequest)
                .queryParam("imperial-units", true)
                .when()
                .post("/indoor-analytics/processes/heating/target-relative-humidity")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(HeatingProcessResponse.class);

        // Then
        assertThat(heatingProcessResponse).isNotNull();
        assertThat(heatingProcessResponseImperial).isNotNull();

        assertThat(heatingProcessResponse).isEqualTo(heatingProcessResponseImperial);

        OutletAirFlowResponse outletAirFlowResponse = heatingProcessResponse.outletAirFlow();

        assertThat(heatingProcessResponse.heatingStrategy()).isEqualTo("HeatingFromRH");
        assertThat(heatingProcessResponse.heatingPower().getValue()).isEqualTo(162559.69, withPrecision(0.01));


        assertThat(outletAirFlowResponse.outletPressure()).isEqualTo(inletPressure);
        assertThat(outletAirFlowResponse.outletHumidityRatio()).isEqualTo(inletHumidityRatio);
        assertThat(outletAirFlowResponse.outletMassFlow()).isEqualTo(flowOfHumidAir.getMassFlow());
        assertThat(outletAirFlowResponse.outletDryAirMassFlow()).isEqualTo(flowOfHumidAir.getDryAirMassFlow());
        assertThat(outletAirFlowResponse.outletRelativeHumidity().getValue()).isEqualTo(targetRelativeHumidity.getValue(), withPrecision(1E-10));

        assertThat(outletAirFlowResponse.outletTemperature().getValue()).isEqualTo(0.93, withPrecision(0.01));
        assertThat(outletAirFlowResponse.outletSpecificEnthalpy().getValue()).isEqualTo(2.45, withPrecision(0.01));

    }

}