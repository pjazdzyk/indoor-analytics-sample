package com.synerset.indooranalyticsapi.property;

import com.synerset.indooranalyticsapi.common.InvalidResponse;
import com.synerset.indooranalyticsapi.common.SwaggerApiRoot;
import com.synerset.indooranalyticsapi.property.data.DryAirResponse;
import com.synerset.indooranalyticsapi.property.data.HumidAirResponse;
import com.synerset.unitility.unitsystem.humidity.HumidityRatio;
import com.synerset.unitility.unitsystem.humidity.RelativeHumidity;
import com.synerset.unitility.unitsystem.thermodynamic.Pressure;
import com.synerset.unitility.unitsystem.thermodynamic.SpecificEnthalpy;
import com.synerset.unitility.unitsystem.thermodynamic.Temperature;
import com.synerset.unitility.validation.PhysicalRange;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Represents a REST service for calculating physical properties of air.
 * This service provides endpoints for calculating properties of dry air and humid air.
 */
@Validated
@Tag(name = "Physical properties service:")
@RequestMapping("/properties")
public interface PhysicalPropertiesRestService extends SwaggerApiRoot {

    String DEFAULT_PRESSURE = "101325.0Pa";

    /**
     * Calculates dry air properties based on user input, based on humid air dry bulb temperature.
     * This calculation can have quite wide temperature and pressure ranges, exceeding typical HVAC cases.
     *
     * @param temperature   The temperature of the dry air.
     * @param pressure      The pressure of the dry air.
     * @param imperialUnits Indicates whether to use imperial units for output.
     * @return The response containing the properties of dry air.
     */
    @Operation(summary = "Calculates dry air properties based on user input.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calculation successful.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DryAirResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input data, calculations not possible.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidResponse.class))})})
    @GetMapping("/dry-air")
    DryAirResponse getDryAirProperties(@RequestParam
                                       @Schema(example = "20oC")
                                       @PhysicalRange(min = "-150oC", max = "1000oC")
                                       Temperature temperature,
                                       @RequestParam(defaultValue = DEFAULT_PRESSURE)
                                       @Schema(example = DEFAULT_PRESSURE)
                                       @PhysicalRange(min = "1000Pa", max = "10MPa")
                                       Pressure pressure,
                                       @RequestParam(name = "imperial-units", defaultValue = "false")
                                       boolean imperialUnits);

    /**
     * Calculates humid air properties based on user input with temperature input as DBT (Dry Bulb Temperature).
     * These calculations have some limitations, accurate results are guaranteed for parameters withing typical HVAC
     * applications. Special consideration has to be made for an attempt to calculate humid air properties for high
     * temperatures (>80 oC) with large humidity content. When saturation pressure of water vapour exceed absolute
     * atmospheric pressure, calculations will not be possible and will result in error.
     *
     * @param temperature      The dry bulb temperature of the humid air.
     * @param pressure         The pressure of the humid air.
     * @param humidityRatio    The humidity ratio of the humid air (optional).
     * @param relativeHumidity The relative humidity of the humid air (optional).
     * @param imperialUnits    Indicates whether to use imperial units for output.
     * @return The response containing the properties of humid air.
     */
    @Operation(summary = "Calculates humid air properties based on user input. Temperature input as DBT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calculation successful.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = HumidAirResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input data, calculations not possible.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidResponse.class))})})
    @GetMapping("/humid-air")
    HumidAirResponse getHumidAirProperties(@RequestParam
                                           @Schema(example = "20oC")
                                           @PhysicalRange(min = "-150oC", max = "200oC")
                                           Temperature temperature,
                                           @RequestParam(defaultValue = DEFAULT_PRESSURE)
                                           @Schema(example = DEFAULT_PRESSURE)
                                           @PhysicalRange(min = "50_000Pa", max = "5.0MPa")
                                           Pressure pressure,
                                           @RequestParam(required = false, name = "humidity-ratio")
                                           @PhysicalRange(min = "0.0kg/kg", max = "3.0kg/kg")
                                           HumidityRatio humidityRatio,
                                           @RequestParam(required = false, name = "relative-humidity")
                                           @Schema(example = "40.5%")
                                           @PhysicalRange(min = "0%", max = "100%")
                                           RelativeHumidity relativeHumidity,
                                           @RequestParam(name = "imperial-units", defaultValue = "false")
                                           boolean imperialUnits);

    /**
     * Calculates humid air properties based on user input for calculated from wet bulb temperature (DBT is unknown).
     * Please note that these calculations are much less stable, as the result must be determined by use of iterative
     * solver. Make sure that your input data are within typical HVAC parameters.
     *
     * @param wetBulbTemperature The wet bulb temperature of the humid air.
     * @param relativeHumidity   The relative humidity of the humid air (optional).
     * @param pressure           The pressure of the humid air.
     * @param imperialUnits      Indicates whether to use imperial units for output.
     * @return The response containing the properties of humid air.
     */
    @Operation(summary = "Calculates humid air properties based on user input. Temperature input as WBT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calculation successful.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = HumidAirResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input data, calculations not possible.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidResponse.class))})})
    @GetMapping("/humid-air/from-wet-bulb")
    HumidAirResponse getHumidAirPropertiesFromWbt(@RequestParam(name = "wet-bulb-temperature")
                                                  @Schema(example = "30.0oC")
                                                  @PhysicalRange(min = "-120oC", max = "100oC")
                                                  Temperature wetBulbTemperature,
                                                  @RequestParam(required = false, name = "relative-humidity")
                                                  @Schema(example = "40.0%")
                                                  @PhysicalRange(min = "0%", max = "100%")
                                                  RelativeHumidity relativeHumidity,
                                                  @RequestParam(defaultValue = DEFAULT_PRESSURE)
                                                  @Schema(example = DEFAULT_PRESSURE)
                                                  @PhysicalRange(min = "50_000Pa", max = "5.0MPa")
                                                  Pressure pressure,
                                                  @RequestParam(name = "imperial-units", defaultValue = "false")
                                                  boolean imperialUnits);

    /**
     * Calculates humid air properties based on user input for calculated from dew point temperature (DBT is unknown).
     * Please note that these calculations are much less stable, as the result must be determined by use of iterative
     * solver. Make sure that your input data are within typical HVAC parameters.
     *
     * @param dewPointTemperature      The dew point temperature of the humid air.
     * @param relativeHumidity The relative humidity of the humid air.
     * @param pressure         The pressure of the humid air.
     * @param imperialUnits    Indicates whether to use imperial units for output.
     * @return The response containing the properties of humid air.
     */
    @Operation(summary = "Calculates humid air properties based on user input. Temperature input as DP.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calculation successful.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = HumidAirResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input data, calculations not possible.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidResponse.class))})})
    @GetMapping("/humid-air/from-dew-point")
    HumidAirResponse getHumidAirPropertiesFromTdp(@RequestParam(name = "dew-point-temperature")
                                                  @Schema(example = "10.0oC")
                                                  @PhysicalRange(min = "-150oC", max = "100oC")
                                                  Temperature dewPointTemperature,
                                                  @RequestParam(name = "relative-humidity")
                                                  @Schema(example = "40.0%")
                                                  @PhysicalRange(min = "0%", max = "100%")
                                                  RelativeHumidity relativeHumidity,
                                                  @RequestParam(defaultValue = DEFAULT_PRESSURE)
                                                  @Schema(example = DEFAULT_PRESSURE)
                                                  @PhysicalRange(min = "1000Pa", max = "10MPa")
                                                  Pressure pressure,
                                                  @RequestParam(name = "imperial-units", defaultValue = "false")
                                                  boolean imperialUnits);

    /**
     * Calculates humid air properties based on user input for calculated from specific enthalpy (DBT is unknown).
     * Please note that these calculations are much less stable, as the result must be determined by use of iterative
     * solver. Make sure that your input data are within typical HVAC parameters.
     *
     * @param specificEnthalpy The specific enthalpy of the humid air.
     * @param humidityRatio    The humidity ratio of the humid air.
     * @param pressure         The pressure of the humid air.
     * @param imperialUnits    Indicates whether to use imperial units for output.
     * @return The response containing the properties of humid air.
     */
    @Operation(summary = "Calculates humid air properties based on user input. DBT is unknown, calculated from specific enthalpy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calculation successful.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = HumidAirResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input data, calculations not possible.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidResponse.class))})})
    @GetMapping("/humid-air/from-enthalpy")
    HumidAirResponse getHumidAirPropertiesFromIx(@RequestParam(name = "specific-enthalpy")
                                                 @Schema(example = "20.8kJ/kg")
                                                 @PhysicalRange(min = "-150kJ/kg", max = "100kJ/kg")
                                                 SpecificEnthalpy specificEnthalpy,
                                                 @RequestParam(name = "humidity-ratio")
                                                 @Schema(example = "0.003kg/kg")
                                                 @PhysicalRange(min = "0.0kg/kg", max = "2.0kg/kg")
                                                 HumidityRatio humidityRatio,
                                                 @RequestParam(defaultValue = DEFAULT_PRESSURE)
                                                 @Schema(example = DEFAULT_PRESSURE)
                                                 @PhysicalRange(min = "1000Pa", max = "10MPa")
                                                 Pressure pressure,
                                                 @RequestParam(name = "imperial-units", defaultValue = "false")
                                                 boolean imperialUnits);

    /**
     * Calculates humid air properties based on user input for calculated from humidity ratio or relative humidity (DBT is unknown).
     * Please note that these calculations are much less stable, as the result must be determined by use of iterative
     * solver. Make sure that your input data are within typical HVAC parameters.
     *
     * @param humidityRatio    The humidity ratio of the humid air.
     * @param relativeHumidity The relative humidity of the humid air.
     * @param pressure         The pressure of the humid air.
     * @param imperialUnits    Indicates whether to use imperial units for output.
     * @return The response containing the properties of humid air.
     */
    @Operation(summary = "Calculates humid air properties based on user input. DBT is unknown, calculated from humidity.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calculation successful.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = HumidAirResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input data, calculations not possible.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidResponse.class))})})
    @GetMapping("/humid-air/from-humidity")
    HumidAirResponse getHumidAirPropertiesFromXRh(@RequestParam(name = "humidity-ratio")
                                                  @Schema(example = "0.0073kg/kg")
                                                  @PhysicalRange(min = "0.0kg/kg", max = "2.0kg/kg")
                                                  HumidityRatio humidityRatio,
                                                  @RequestParam(name = "relative-humidity")
                                                  @Schema(example = "50%")
                                                  @PhysicalRange(min = "0%", max = "100%")
                                                  RelativeHumidity relativeHumidity,
                                                  @RequestParam(defaultValue = DEFAULT_PRESSURE)
                                                  @Schema(example = DEFAULT_PRESSURE)
                                                  @PhysicalRange(min = "1000Pa", max = "10MPa")
                                                  Pressure pressure,
                                                  @RequestParam(name = "imperial-units", defaultValue = "false")
                                                  boolean imperialUnits);

}