package com.synerset.indooranalytics.domain.process;

import com.synerset.hvacengine.fluids.humidair.FlowOfHumidAir;
import com.synerset.hvacengine.process.heating.Heating;
import com.synerset.unitility.unitsystem.humidity.RelativeHumidity;
import com.synerset.unitility.unitsystem.thermodynamic.Power;
import com.synerset.unitility.unitsystem.thermodynamic.Temperature;

/**
 * Port for computing heating processes service.
 */
public interface HeatingProcessPort {

    /**
     * Computes heating for a specified input power.
     *
     * @param inletFlow    The inlet flow of humid air.
     * @param inputPower   The input power for heating.
     * @return             The heating process.
     */
    Heating computeHeatingForInputPower(FlowOfHumidAir inletFlow, Power inputPower);

    /**
     * Computes heating to achieve a desired target temperature.
     *
     * @param inletFlow          The inlet flow of humid air.
     * @param targetTemperature  The target temperature for heating.
     * @return                   The heating process.
     */
    Heating computeHeatingForTargetTemperature(FlowOfHumidAir inletFlow, Temperature targetTemperature);

    /**
     * Computes heating to achieve a desired target relative humidity.
     *
     * @param inletFlow          The inlet flow of humid air.
     * @param relativeHumidity   The target relative humidity for heating.
     * @return                   The heating process.
     */
    Heating computeHeatingForTargetRelativeHumidity(FlowOfHumidAir inletFlow, RelativeHumidity relativeHumidity);

    /**
     * Creates an instance of the HeatingProcessPort service.
     *
     * @return The HeatingProcessPort instance.
     */
    static HeatingProcessPort create() {
        return new HeatingProcessService();
    }

}
