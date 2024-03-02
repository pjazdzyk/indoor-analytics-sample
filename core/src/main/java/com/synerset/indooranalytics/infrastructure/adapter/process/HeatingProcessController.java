package com.synerset.indooranalytics.infrastructure.adapter.process;

import com.synerset.hvacengine.fluids.humidair.FlowOfHumidAir;
import com.synerset.hvacengine.process.heating.Heating;
import com.synerset.indooranalytics.domain.process.HeatingProcessPort;
import com.synerset.indooranalytics.infrastructure.exceptionhandling.IndoorAnalyticsInvalidArgumentException;
import com.synerset.indooranalyticsapi.process.heating.HeatingProcessRestService;
import com.synerset.indooranalyticsapi.process.heating.data.HeatingProcessRequest;
import com.synerset.indooranalyticsapi.process.heating.data.HeatingProcessResponse;
import com.synerset.unitility.unitsystem.humidity.RelativeHumidity;
import com.synerset.unitility.unitsystem.thermodynamic.Temperature;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HeatingProcessController implements HeatingProcessRestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeatingProcessController.class);
    private final HeatingProcessPort heatingService;
    private final ProcessControllerMapper processMapper;
    private final MeterRegistry meterRegistry;

    public HeatingProcessController(HeatingProcessPort heatingService,
                                    ProcessControllerMapper processMapper,
                                    MeterRegistry meterRegistry) {

        this.heatingService = heatingService;
        this.processMapper = processMapper;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public HeatingProcessResponse getHeatingForInputPower(HeatingProcessRequest heatingRequest, boolean imperialUnits) {
        LOGGER.debug("[REST CALL]: Requested heating of humid air for input power: {}", heatingRequest);
        meterRegistry.counter("process-heating-input-power").increment();
        validateInputPowerRequirements(heatingRequest);
        FlowOfHumidAir inletFlow = processMapper.toFlowOfHumidAir(heatingRequest.inletAirFlow());
        Heating heatingProcess = heatingService.computeHeatingForInputPower(inletFlow, heatingRequest.inputHeatingPower());
        HeatingProcessResponse heatingResponse = processMapper.toHeatingResponse(heatingProcess);
        return imperialUnits ? heatingResponse.toImperialUnits() : heatingResponse;
    }

    @Override
    public HeatingProcessResponse getHeatingForTargetTemperature(HeatingProcessRequest heatingRequest, boolean imperialUnits) {
        LOGGER.debug("[REST CALL]: Requested heating of humid air for target temperature: {}", heatingRequest);
        meterRegistry.counter("process-heating-target-temperature").increment();
        validateHeatingTemperatureRequirement(heatingRequest);
        FlowOfHumidAir inletFlow = processMapper.toFlowOfHumidAir(heatingRequest.inletAirFlow());
        Heating heatingProcess = heatingService.computeHeatingForTargetTemperature(inletFlow, heatingRequest.targetTemperature());
        HeatingProcessResponse heatingResponse = processMapper.toHeatingResponse(heatingProcess);
        return imperialUnits ? heatingResponse.toImperialUnits() : heatingResponse;
    }

    @Override
    public HeatingProcessResponse getHeatingForTargetRelativeHumidity(HeatingProcessRequest heatingRequest, boolean imperialUnits) {
        LOGGER.debug("[REST CALL]: Requested heating of humid air for input data for relative humidity: {}", heatingRequest);
        meterRegistry.counter("process-heating-target-relative-humidity").increment();
        validateHeatingHumidityRequirement(heatingRequest);
        FlowOfHumidAir inletFlow = processMapper.toFlowOfHumidAir(heatingRequest.inletAirFlow());
        Heating heatingProcess = heatingService.computeHeatingForTargetRelativeHumidity(inletFlow, heatingRequest.targetRelativeHumidity());
        HeatingProcessResponse heatingResponse = processMapper.toHeatingResponse(heatingProcess);
        return imperialUnits ? heatingResponse.toImperialUnits() : heatingResponse;
    }

    private void validateInputPowerRequirements(HeatingProcessRequest heatingRequest) {
        if (heatingRequest.inputHeatingPower() == null) {
            throw new IndoorAnalyticsInvalidArgumentException("Invalid request. Heating power must be not null");
        }
    }

    private void validateHeatingTemperatureRequirement(HeatingProcessRequest heatingRequest) {
        Temperature targetTemp = heatingRequest.targetTemperature();
        Temperature inletTemp = heatingRequest.inletAirFlow().inletTemperature();

        if (targetTemp == null) {
            throw new IndoorAnalyticsInvalidArgumentException("Invalid request. Target temperature must be not null");
        }

        if (targetTemp.isLowerThan(inletTemp)) {
            throw new IndoorAnalyticsInvalidArgumentException("Invalid request. Target temperature must be greater than " +
                                                              "inlet temperature for heating process. " +
                                                              "t_target = " + targetTemp + ", t_inlet = " + inletTemp);
        }
    }

    private void validateHeatingHumidityRequirement(HeatingProcessRequest heatingRequest) {
        RelativeHumidity targetRH = heatingRequest.targetRelativeHumidity();
        RelativeHumidity inletRH = heatingRequest.inletAirFlow().inletRelativeHumidity();

        if (targetRH == null) {
            throw new IndoorAnalyticsInvalidArgumentException("Invalid request. Target relative humidity must be not null");
        }

        if (targetRH.isGreaterThan(inletRH)) {
            throw new IndoorAnalyticsInvalidArgumentException("Invalid request. Target relative humidity must be lower than " +
                                                              "inlet relative humidity in heating process. " +
                                                              "RH_target = " + targetRH + ", RH_inlet = " + inletRH);
        }

    }

}
