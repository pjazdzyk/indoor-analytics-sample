package com.synerset.indooranalyticsapi.process.heating;

class HeatingRequestExamples {

    private HeatingRequestExamples() {
        throw new IllegalStateException("Utility class");
    }

    static final String HEATING_FROM_POWER_EXAMPLE = """
              {
                "inputHeatingPower": {"value": 10, "unit": "kW"},
                "inletAirFlow":{
                  "inletPressure": {"value": 101325.0, "unit": "Pa"},
                  "inletTemperature": {"value": -20, "unit": "oC"},
                  "inletRelativeHumidity": {"value": 95,"unit": "%"},
                  "inletVolFlow": {"value": 30000, "unit": "m3/h"}
                }
              }
            """;

    static final String HEATING_FROM_TEMP_EXAMPLE = """
              {
                "targetTemperature": {"value": 22, "unit": "oC"},
                "inletAirFlow":{
                  "inletPressure": {"value": 101325.0, "unit": "Pa"},
                  "inletTemperature": {"value": -20, "unit": "oC"},
                  "inletRelativeHumidity": {"value": 95,"unit": "%"},
                  "inletVolFlow": {"value": 30000, "unit": "m3/h"}
                }
              }
            """;

    static final String HEATING_FROM_REL_HUM_EXAMPLE = """
              {
                "targetRelativeHumidity": {"value": 30, "unit": "%"},
                "inletAirFlow":{
                  "inletPressure": {"value": 101325.0, "unit": "Pa"},
                  "inletTemperature": {"value": -20, "unit": "oC"},
                  "inletRelativeHumidity": {"value": 95,"unit": "%"},
                  "inletVolFlow": {"value": 30000, "unit": "m3/h"}
                }
              }
            """;

}
