package com.synerset.indooranalytics.infrastructure.adapter.process;

import com.synerset.hvacengine.fluids.FlowEquations;
import com.synerset.hvacengine.fluids.humidair.FlowOfHumidAir;
import com.synerset.hvacengine.fluids.humidair.HumidAir;
import com.synerset.hvacengine.fluids.humidair.VapourState;
import com.synerset.hvacengine.process.heating.Heating;
import com.synerset.hvacengine.process.heating.HeatingStrategy;
import com.synerset.indooranalyticsapi.process.common.InletAirFlowRequest;
import com.synerset.indooranalyticsapi.process.common.OutletAirFlowResponse;
import com.synerset.indooranalyticsapi.process.heating.data.HeatingProcessResponse;
import com.synerset.unitility.unitsystem.flow.MassFlow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
interface ProcessControllerMapper {

    @Mapping(target = "inletPressure", source = "fluid.pressure")
    @Mapping(target = "inletTemperature", source = "fluid.temperature")
    @Mapping(target = "inletRelativeHumidity", source = "fluid.relativeHumidity")
    @Mapping(target = "inletHumidityRatio", source = "fluid.humidityRatio")
    @Mapping(target = "inletVolFlow", source = "volFlow")
    @Mapping(target = "inletMassFlow", source = "massFlow")
    InletAirFlowRequest toInletFlowRequest(FlowOfHumidAir flowOfHumidAir);

    @Mapping(target = "outletMassFlow", source = "massFlow")
    @Mapping(target = "outletDryAirMassFlow", source = "flowOfDryAir.massFlow")
    @Mapping(target = "outletVolFlow", source = "volFlow")
    @Mapping(target = "outletPressure", source = "fluid.pressure")
    @Mapping(target = "outletTemperature", source = "fluid.temperature")
    @Mapping(target = "outletRelativeHumidity", source = "fluid.relativeHumidity")
    @Mapping(target = "outletHumidityRatio", source = "fluid.humidityRatio")
    @Mapping(target = "outletSpecificEnthalpy", source = "fluid.specificEnthalpy")
    @Mapping(target = "outletVapourState", source = "fluid.vapourState")
    OutletAirFlowResponse toOutletFlowResponse(FlowOfHumidAir flowOfHumidAir);

    @Mapping(target = "heatingPower", source = "heatOfProcess")
    @Mapping(target = "outletAirFlow", source = "outletFlow")
    HeatingProcessResponse toHeatingResponse(Heating heating);

    default FlowOfHumidAir toFlowOfHumidAir(InletAirFlowRequest inletAirFlowRequest) {
        HumidAir humidAir = inletAirFlowRequest.inletHumidityRatio() == null
                ? HumidAir.of(inletAirFlowRequest.inletPressure(), inletAirFlowRequest.inletTemperature(), inletAirFlowRequest.inletRelativeHumidity())
                : HumidAir.of(inletAirFlowRequest.inletPressure(), inletAirFlowRequest.inletTemperature(), inletAirFlowRequest.inletHumidityRatio());

        MassFlow massFlow = inletAirFlowRequest.inletMassFlow() == null
                ? FlowEquations.volFlowToMassFlow(humidAir.getDensity(), inletAirFlowRequest.inletVolFlow())
                : inletAirFlowRequest.inletMassFlow();

        return FlowOfHumidAir.of(humidAir, massFlow);
    }

    default String toHeatingStrategyAsString(HeatingStrategy heatingStrategy) {
        if (heatingStrategy == null) {
            return null;
        }
        return heatingStrategy.getClass().getSimpleName();
    }

    default String toVapourStateAsString(VapourState vapourState) {
        if (vapourState == null) {
            return null;
        }
        return vapourState.toString();
    }

}