package com.synerset.indooranalyticsapi.process.heating;

import com.synerset.indooranalyticsapi.common.InvalidResponse;
import com.synerset.indooranalyticsapi.common.SwaggerApiRoot;
import com.synerset.indooranalyticsapi.process.heating.data.HeatingProcessRequest;
import com.synerset.indooranalyticsapi.process.heating.data.HeatingProcessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.synerset.indooranalyticsapi.process.heating.HeatingRequestExamples.HEATING_FROM_POWER_EXAMPLE;
import static com.synerset.indooranalyticsapi.process.heating.HeatingRequestExamples.HEATING_FROM_REL_HUM_EXAMPLE;
import static com.synerset.indooranalyticsapi.process.heating.HeatingRequestExamples.HEATING_FROM_TEMP_EXAMPLE;

@Tag(name = "Heating process service:")
@RequestMapping("/processes/heating")
public interface HeatingProcessRestService extends SwaggerApiRoot {

    String HEATING_DESCRIPTION = """
               Pressure specification is optional. In the absence of explicit input, the standard atmospheric pressure
               of [101325.0 Pa] shall be used in calculations. Humidity may be denoted either as inletRelativeHumidity or
               inletHumidityRatio; in their absence, dry air scenario will be assumed.
               Flow of inlet air can be specified either as inletVolFlow or inletMassFlow. Absence of both means that
               zero flow is assumed, what will result in no state change of the inlet air.;
            """;

    /**
     * Calculates the outcome of heating humid air for a specified input heating power.
     *
     * @param heatingRequest The request containing heating process parameters.
     * @param imperialUnits  Indicates whether to use imperial units for output.
     * @return The response containing the outcome of heating.
     */
    @Operation(summary = "Calculates the outcome of heating humid air for a specified input heating power.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = HEATING_DESCRIPTION,
                    content = @Content(examples = @ExampleObject(HEATING_FROM_POWER_EXAMPLE))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calculation successful.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = HeatingProcessResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input data, calculations not possible.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidResponse.class))})})
    @PostMapping("/input-power")
    HeatingProcessResponse getHeatingForInputPower(@RequestBody @Valid HeatingProcessRequest heatingRequest,
                                                   @RequestParam(name = "imperial-units", defaultValue = "false") boolean imperialUnits);

    /**
     * Calculates the outcome of heating humid air to achieve a desired outlet temperature.
     *
     * @param heatingRequest The request containing heating process parameters.
     * @param imperialUnits  Indicates whether to use imperial units for output.
     * @return The response containing the outcome of heating.
     */
    @Operation(summary = "Calculates the outcome of heating humid air to achieve a desired outlet temperature.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = HEATING_DESCRIPTION,
                    content = @Content(examples = @ExampleObject(HEATING_FROM_TEMP_EXAMPLE))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calculation successful.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = HeatingProcessResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input data, calculations not possible.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidResponse.class))})})
    @PostMapping("/target-temperature")
    HeatingProcessResponse getHeatingForTargetTemperature(@RequestBody @Valid HeatingProcessRequest heatingRequest,
                                                          @RequestParam(name = "imperial-units", defaultValue = "false") boolean imperialUnits);

    /**
     * Calculates the outcome of heating humid air to achieve a desired outlet relative humidity.
     *
     * @param heatingRequest The request containing heating process parameters.
     * @param imperialUnits  Indicates whether to use imperial units for output.
     * @return The response containing the outcome of heating.
     */
    @Operation(summary = "Calculates the outcome of heating humid air to achieve a desired outlet relative humidity.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = HEATING_DESCRIPTION,
                    content = @Content(examples = @ExampleObject(HEATING_FROM_REL_HUM_EXAMPLE))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calculation successful.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = HeatingProcessResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input data, calculations not possible.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidResponse.class))})})
    @PostMapping("/target-relative-humidity")
    HeatingProcessResponse getHeatingForTargetRelativeHumidity(@RequestBody @Valid HeatingProcessRequest heatingRequest,
                                                               @RequestParam(name = "imperial-units", defaultValue = "false") boolean imperialUnits);

}