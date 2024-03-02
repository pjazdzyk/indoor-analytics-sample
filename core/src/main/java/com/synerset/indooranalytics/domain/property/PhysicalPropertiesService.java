package com.synerset.indooranalytics.domain.property;

import com.synerset.hvacengine.fluids.dryair.DryAir;
import com.synerset.hvacengine.fluids.humidair.HumidAir;
import com.synerset.hvacengine.fluids.humidair.HumidAirEquations;
import com.synerset.unitility.unitsystem.humidity.HumidityRatio;
import com.synerset.unitility.unitsystem.humidity.RelativeHumidity;
import com.synerset.unitility.unitsystem.thermodynamic.Pressure;
import com.synerset.unitility.unitsystem.thermodynamic.SpecificEnthalpy;
import com.synerset.unitility.unitsystem.thermodynamic.Temperature;

class PhysicalPropertiesService implements PhysicalPropertiesPort {

    @Override
    public DryAir getDryAirProperties(Pressure pressure, Temperature temperature) {
        return DryAir.of(pressure, temperature);
    }

    @Override
    public HumidAir getHumidAirProperties(Pressure pressure, Temperature temperature, HumidityRatio humidityRatio) {
        return HumidAir.of(pressure, temperature, humidityRatio);
    }

    @Override
    public HumidAir getHumidAirProperties(Pressure pressure, Temperature temperature, RelativeHumidity relativeHumidity) {
        return HumidAir.of(pressure, temperature, relativeHumidity);
    }

    @Override
    public HumidAir getHumidAirPropertiesFromWbt(Pressure pressure, Temperature wetBulbTemperature, RelativeHumidity relativeHumidity) {
        Temperature dryBulbTemperature = HumidAirEquations.dryBulbTemperatureWbtRH(wetBulbTemperature, relativeHumidity, pressure);
        return HumidAir.of(pressure, dryBulbTemperature, relativeHumidity);
    }

    @Override
    public HumidAir getHumidAirPropertiesFromTdp(Pressure pressure, Temperature dewPointTemperature, RelativeHumidity relativeHumidity) {
        Temperature dryBulbTemperature = HumidAirEquations.dryBulbTemperatureTdpRH(dewPointTemperature, relativeHumidity, pressure);
        return HumidAir.of(pressure, dryBulbTemperature, relativeHumidity);
    }

    @Override
    public HumidAir getHumidAirPropertiesFromIx(Pressure pressure, SpecificEnthalpy specificEnthalpy, HumidityRatio humidityRatio) {
        Temperature dryBulbTemperature = HumidAirEquations.dryBulbTemperatureIX(specificEnthalpy, humidityRatio, pressure);
        return HumidAir.of(pressure, dryBulbTemperature, humidityRatio);
    }

    @Override
    public HumidAir getHumidAirPropertiesFromXRh(Pressure pressure, HumidityRatio humidityRatio, RelativeHumidity relativeHumidity) {
        Temperature dryBulbTemperature = HumidAirEquations.dryBulbTemperatureXRH(humidityRatio, relativeHumidity, pressure);
        return HumidAir.of(pressure, dryBulbTemperature, humidityRatio);
    }

}
