package com.synerset.indooranalytics.infrastructure.adapter.property;

import com.synerset.hvacengine.fluids.dryair.DryAir;
import com.synerset.hvacengine.fluids.humidair.HumidAir;
import com.synerset.indooranalytics.domain.property.PhysicalPropertiesPort;
import com.synerset.indooranalyticsapi.property.PhysicalPropertiesRestService;
import com.synerset.indooranalyticsapi.property.data.DryAirResponse;
import com.synerset.indooranalyticsapi.property.data.HumidAirResponse;
import com.synerset.unitility.unitsystem.humidity.HumidityRatio;
import com.synerset.unitility.unitsystem.humidity.RelativeHumidity;
import com.synerset.unitility.unitsystem.thermodynamic.Pressure;
import com.synerset.unitility.unitsystem.thermodynamic.SpecificEnthalpy;
import com.synerset.unitility.unitsystem.thermodynamic.Temperature;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
class PhysicalPropertiesController implements PhysicalPropertiesRestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhysicalPropertiesController.class);
    private final PhysicalPropertiesMapper propertiesMapper;
    private final PhysicalPropertiesPort propertiesService;
    private final MeterRegistry meterRegistry;

    PhysicalPropertiesController(PhysicalPropertiesMapper propertiesMapper,
                                 PhysicalPropertiesPort propertiesService,
                                 MeterRegistry meterRegistry) {

        this.propertiesMapper = propertiesMapper;
        this.propertiesService = propertiesService;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public DryAirResponse getDryAirProperties(Temperature temperature, Pressure pressure, boolean imperialUnits) {
        LOGGER.debug("[REST CALL]: Requested dry air properties for: ta = {}, p_abs = {}", temperature, pressure);
        meterRegistry.counter("property-dry-air").increment();
        DryAir dryAir = propertiesService.getDryAirProperties(pressure, temperature);
        DryAirResponse dryAirResponse = propertiesMapper.toDryAirResponse(dryAir);
        return imperialUnits ? dryAirResponse.toImperialUnits() : dryAirResponse;
    }

    @Override
    public HumidAirResponse getHumidAirProperties(Temperature temperature,
                                                  Pressure pressure,
                                                  HumidityRatio humidityRatio,
                                                  RelativeHumidity relativeHumidity,
                                                  boolean imperialUnits) {

        LOGGER.debug("[REST CALL]: Requested humid air properties based on dry bulb temperature (DBT) for: " +
                "t_dbt = {}, p_abs = {}, x = {}, RH = {}", temperature, pressure, humidityRatio, relativeHumidity);
        meterRegistry.counter("property-humid-air").increment();

        if (humidityRatio == null && relativeHumidity == null) {
            HumidAir humidAir = propertiesService.getHumidAirProperties(pressure, temperature, HumidityRatio.HUM_RATIO_MIN_LIMIT);
            HumidAirResponse humidAirResponse = propertiesMapper.toHumidAirResponse(humidAir);
            return imperialUnits ? humidAirResponse.toImperialUnits() : humidAirResponse;
        }

        HumidAir humidAir = humidityRatio == null
                ? propertiesService.getHumidAirProperties(pressure, temperature, relativeHumidity)
                : propertiesService.getHumidAirProperties(pressure, temperature, humidityRatio);

        HumidAirResponse humidAirResponse = propertiesMapper.toHumidAirResponse(humidAir);
        return imperialUnits ? humidAirResponse.toImperialUnits() : humidAirResponse;
    }

    @Override
    public HumidAirResponse getHumidAirPropertiesFromWbt(Temperature wetBulbTemperature,
                                                         RelativeHumidity relativeHumidity,
                                                         Pressure pressure,
                                                         boolean imperialUnits) {

        LOGGER.debug("[REST CALL]: Requested humid air properties derived from wet bulb temperature (WBT) for: " +
                "t_wbt = {}, p_abs = {}, RH = {}", wetBulbTemperature, pressure, relativeHumidity);
        meterRegistry.counter("property-humid-air-from-wbt").increment();

        RelativeHumidity inputRelHum = relativeHumidity == null ? RelativeHumidity.RH_MIN_LIMIT : relativeHumidity;
        HumidAir humidAir = propertiesService.getHumidAirPropertiesFromWbt(pressure, wetBulbTemperature, inputRelHum);
        HumidAirResponse humidAirResponse = propertiesMapper.toHumidAirResponse(humidAir);
        return imperialUnits ? humidAirResponse.toImperialUnits() : humidAirResponse;
    }

    @Override
    public HumidAirResponse getHumidAirPropertiesFromTdp(Temperature dewPointTemperature,
                                                         RelativeHumidity relativeHumidity,
                                                         Pressure pressure,
                                                         boolean imperialUnits) {

        LOGGER.debug("[REST CALL]: Requested humid air properties derived from dew point temperature (DP) for: " +
                        "t_dp = {}, p_abs = {}, RH = {}", dewPointTemperature, pressure, relativeHumidity);
        meterRegistry.counter("property-humid-from-tdp").increment();

        RelativeHumidity inputRelHum = relativeHumidity == null ? RelativeHumidity.RH_MIN_LIMIT : relativeHumidity;
        HumidAir humidAir = propertiesService.getHumidAirPropertiesFromTdp(pressure, dewPointTemperature, inputRelHum);
        HumidAirResponse humidAirResponse = propertiesMapper.toHumidAirResponse(humidAir);
        return imperialUnits ? humidAirResponse.toImperialUnits() : humidAirResponse;
    }

    @Override
    public HumidAirResponse getHumidAirPropertiesFromIx(SpecificEnthalpy specificEnthalpy,
                                                        HumidityRatio humidityRatio,
                                                        Pressure pressure,
                                                        boolean imperialUnits) {

        LOGGER.debug("[REST CALL]: Requested humid air properties derived from a specific enthalpy (h) for: " +
                "h = {}, p_abs = {}, x = {}", specificEnthalpy, pressure, humidityRatio);
        meterRegistry.counter("property-humid-air-from-ix").increment();

        HumidAir humidAir = propertiesService.getHumidAirPropertiesFromIx(pressure, specificEnthalpy, humidityRatio);
        HumidAirResponse humidAirResponse = propertiesMapper.toHumidAirResponse(humidAir);
        return imperialUnits ? humidAirResponse.toImperialUnits() : humidAirResponse;
    }

    @Override
    public HumidAirResponse getHumidAirPropertiesFromXRh(HumidityRatio humidityRatio,
                                                         RelativeHumidity relativeHumidity,
                                                         Pressure pressure,
                                                         boolean imperialUnits) {

        LOGGER.debug("[REST CALL]: Requested humid air properties derived from a humidity (x or RH) for: " +
                "p_abs = {}, x = {}, RH = {}", pressure, humidityRatio, relativeHumidity);
        meterRegistry.counter("property-humid-air-from-xrh").increment();

        HumidAir humidAir = propertiesService.getHumidAirPropertiesFromXRh(pressure, humidityRatio, relativeHumidity);
        HumidAirResponse humidAirResponse = propertiesMapper.toHumidAirResponse(humidAir);
        return imperialUnits ? humidAirResponse.toImperialUnits() : humidAirResponse;
    }

}