package com.synerset.indooranalyticsapi.process.common;

import com.synerset.unitility.unitsystem.flow.MassFlow;
import com.synerset.unitility.unitsystem.flow.VolumetricFlow;
import com.synerset.unitility.unitsystem.humidity.HumidityRatio;
import com.synerset.unitility.unitsystem.humidity.RelativeHumidity;
import com.synerset.unitility.unitsystem.thermodynamic.Pressure;
import com.synerset.unitility.unitsystem.thermodynamic.Temperature;
import com.synerset.unitility.validation.PhysicalRange;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.ObjectUtils;

public record InletAirFlowRequest(
        @PhysicalRange(min = "50_000Pa", max = "5.0MPa")
        Pressure inletPressure,
        @NotNull
        @PhysicalRange(min = "-120oC", max = "165c")
        Temperature inletTemperature,
        @PhysicalRange(min = "0%", max = "100%")
        RelativeHumidity inletRelativeHumidity,
        @PhysicalRange(min = "0kg/kg", max = "5.0kg/kg")
        HumidityRatio inletHumidityRatio,
        @PhysicalRange(min = "0.0m3/h", max = "10.E9m3/h")
        VolumetricFlow inletVolFlow,
        @PhysicalRange(min = "0.0kg/h", max = "1.2E9kg/h")
        MassFlow inletMassFlow
) {
    public InletAirFlowRequest {
        if (inletPressure == null) {
            inletPressure = Pressure.STANDARD_ATMOSPHERE;
        }
        if (ObjectUtils.allNull(inletRelativeHumidity, inletHumidityRatio)) {
            inletHumidityRatio = HumidityRatio.HUM_RATIO_MIN_LIMIT;
        }
        if (ObjectUtils.allNull(inletMassFlow, inletVolFlow)) {
            inletMassFlow = MassFlow.MASS_FLOW_MIN_LIMIT;
        }
    }

}