package com.synerset.indooranalytics.domain.property;

import com.synerset.hvacengine.fluids.dryair.DryAir;
import com.synerset.hvacengine.fluids.humidair.HumidAir;
import com.synerset.unitility.unitsystem.humidity.HumidityRatio;
import com.synerset.unitility.unitsystem.humidity.RelativeHumidity;
import com.synerset.unitility.unitsystem.thermodynamic.Pressure;
import com.synerset.unitility.unitsystem.thermodynamic.SpecificEnthalpy;
import com.synerset.unitility.unitsystem.thermodynamic.Temperature;

/**
 * Port interface for service retrieving physical properties of air.
 */
public interface PhysicalPropertiesPort {

    /**
     * Retrieves the properties of dry air based on pressure and temperature.
     *
     * @param pressure    The pressure of the air.
     * @param temperature The temperature of the air.
     * @return The properties of dry air.
     */
    DryAir getDryAirProperties(Pressure pressure, Temperature temperature);

    /**
     * Retrieves the properties of humid air based on pressure, temperature, and humidity ratio.
     *
     * @param pressure       The pressure of the air.
     * @param temperature    The temperature of the air.
     * @param humidityRatio  The humidity ratio of the air.
     * @return The properties of humid air.
     */
    HumidAir getHumidAirProperties(Pressure pressure, Temperature temperature, HumidityRatio humidityRatio);

    /**
     * Retrieves the properties of humid air based on pressure, temperature, and relative humidity.
     *
     * @param pressure          The pressure of the air.
     * @param temperature       The temperature of the air.
     * @param relativeHumidity  The relative humidity of the air.
     * @return The properties of humid air.
     */
    HumidAir getHumidAirProperties(Pressure pressure, Temperature temperature, RelativeHumidity relativeHumidity);

    /**
     * Retrieves the properties of humid air based on pressure, wet bulb temperature, and relative humidity.
     *
     * @param pressure          The pressure of the air.
     * @param wetBulbTemperature  The wet bulb temperature of the air.
     * @param relativeHumidity  The relative humidity of the air.
     * @return The properties of humid air.
     */
    HumidAir getHumidAirPropertiesFromWbt(Pressure pressure, Temperature wetBulbTemperature, RelativeHumidity relativeHumidity);

    /**
     * Retrieves the properties of humid air based on pressure, dew point temperature, and relative humidity.
     *
     * @param pressure          The pressure of the air.
     * @param dewPointTemperature  The dew point temperature of the air.
     * @param relativeHumidity  The relative humidity of the air.
     * @return The properties of humid air.
     */
    HumidAir getHumidAirPropertiesFromTdp(Pressure pressure, Temperature dewPointTemperature, RelativeHumidity relativeHumidity);

    /**
     * Retrieves the properties of humid air based on input pressure, specific enthalpy, and humidity ratio.
     *
     * @param inputPressure       The pressure of the air.
     * @param specificEnthalpy    The specific enthalpy of the air.
     * @param humidityRatio       The humidity ratio of the air.
     * @return The properties of humid air.
     */
    HumidAir getHumidAirPropertiesFromIx(Pressure inputPressure, SpecificEnthalpy specificEnthalpy, HumidityRatio humidityRatio);

    /**
     * Retrieves the properties of humid air based on input pressure, humidity ratio, and relative humidity.
     *
     * @param inputPressure          The pressure of the air.
     * @param humidityRatio          The humidity ratio of the air.
     * @param relativeHumidity       The relative humidity of the air.
     * @return The properties of humid air.
     */
    HumidAir getHumidAirPropertiesFromXRh(Pressure inputPressure, HumidityRatio humidityRatio, RelativeHumidity relativeHumidity);

    /**
     * Static factory method to create an instance of PhysicalPropertiesPort. This is not a singleton pattern.
     * Each create() will provide new instance.
     *
     * @return A new instance of PhysicalPropertiesPort.
     */
    static PhysicalPropertiesPort create(){
        return new PhysicalPropertiesService();
    }
}
