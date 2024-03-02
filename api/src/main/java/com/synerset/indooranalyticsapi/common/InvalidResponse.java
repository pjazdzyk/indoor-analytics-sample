package com.synerset.indooranalyticsapi.common;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;

/**
 * Represents an invalid response object returned by the Indoor Analytics service.
 * This record encapsulates details such as service name, cause of the error, error message, and timestamp.
 */
public record InvalidResponse(
        @Schema(example = "Indoor Analytics")
        String serviceName,
        @Schema(example = "UnitSystemParseException")
        String cause,
        @Schema(example = "Unsupported unit symbol: {xyz}. Target class: TemperatureUnits")
        String message,
        @Schema(example = "2024-02-10T14:39:11.8551038Z")
        ZonedDateTime timestamp
) {}