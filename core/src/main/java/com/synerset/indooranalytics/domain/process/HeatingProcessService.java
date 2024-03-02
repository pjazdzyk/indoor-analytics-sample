package com.synerset.indooranalytics.domain.process;

import com.synerset.hvacengine.fluids.humidair.FlowOfHumidAir;
import com.synerset.hvacengine.process.heating.Heating;
import com.synerset.hvacengine.process.heating.HeatingStrategy;
import com.synerset.unitility.unitsystem.humidity.RelativeHumidity;
import com.synerset.unitility.unitsystem.thermodynamic.Power;
import com.synerset.unitility.unitsystem.thermodynamic.Temperature;

class HeatingProcessService implements HeatingProcessPort {

    @Override
    public Heating computeHeatingForInputPower(FlowOfHumidAir inletFlow, Power inputPower) {
        HeatingStrategy heatingStrategy = HeatingStrategy.of(inletFlow, inputPower);
        return Heating.of(heatingStrategy);
    }

    @Override
    public Heating computeHeatingForTargetTemperature(FlowOfHumidAir inletFlow, Temperature targetTemperature) {
        HeatingStrategy heatingStrategy = HeatingStrategy.of(inletFlow, targetTemperature);
        return Heating.of(heatingStrategy);
    }

    @Override
    public Heating computeHeatingForTargetRelativeHumidity(FlowOfHumidAir inletFlow, RelativeHumidity relativeHumidity) {
        HeatingStrategy heatingStrategy = HeatingStrategy.of(inletFlow, relativeHumidity);
        return Heating.of(heatingStrategy);
    }

}
