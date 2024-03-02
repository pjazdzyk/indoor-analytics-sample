# INDOOR ANALYTICS (IDAS) - HVAC Engineer's Analysis Toolkit

Introducing <strong> INDOOR ANALYTICS</strong>, a REST service integrating HVAC engineering libraries: 
[Hvac|Engine](https://github.com/pjazdzyk/hvac-engine) for the physics of humid air and thermodynamic processes and
[Unitility](https://github.com/pjazdzyk/unitility) Spring module for physical quantity types and conversion and automatic serialization / deserialization 
in web components.

This service is dedicated to HVAC and Mechanical engineers, providing accurate calculations of air properties with humidity
content (Psychrometrics) and thermodynamic processes within the typical parameters scope for HVAC industry applications.<br>

**This is not "yet another psychrometrics calculator."** The entire engineering library ecosystem I have created and
utilized within this project is the result of years of development. Every temperature-dependent property is calculated 
based on equations available in standards, literature, or formulas derived by myself - not assumed as constant.
Core libraries used in this project were developed also based on industry-recognized standards such as ASHRAE Fundamentals,
ensuring precise and accurate results for applications where accuracy is paramount.

IMPORTANT: This repository is created for presentation purposes; it has a reduced scope intended to present the architecture 
and application design. A more advanced version of this application is being developed independently in a private repository 
for commercialization at a later stage.

![Build And Test](https://github.com/pjazdzyk/indoor-analytics-sample/actions/workflows/build-test-analyze.yml/badge.svg) &nbsp;
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=pjazdzyk_indoor-analytics-sample&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=pjazdzyk_indoor-analytics-sample)
&nbsp;
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=pjazdzyk_indoor-analytics-sample&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=pjazdzyk_indoor-analytics-sample)
&nbsp;
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=pjazdzyk_indoor-analytics-sample&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=pjazdzyk_indoor-analytics-sample)
&nbsp;
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=pjazdzyk_indoor-analytics-sample&metric=coverage)](https://sonarcloud.io/summary/new_code?id=pjazdzyk_indoor-analytics-sample)
&nbsp;

> AUTHOR: **Piotr Jazdzyk**, MSc Eng <br>
> LINKEDIN: https://www.linkedin.com/in/pjazdzyk <br>


## TABLE OF CONTENTS
1. [Tech & dependencies](#1-tech-and-dependencies) <br>
2. [Current version](#2-architecture) <br>
3. [Architecture](#3-current-version) <br>
4. [Functionality](#4-functionality) <br>
5. [REST API](#5-rest-api) <br>
   5.1 [Api versioning](#51-versioning) <br>
   5.2 [Physical properties of air](#52-physical-properties-of-humid-air) <br>
   5.3 [Heating process](#53-process-of-heating) <br>
   5.4 [Cooling process](#54-process-of-real-cooling-with-condensate-discharge) <br>
   5.5 [Mixing process](#55-process-of-mixing) <br>
   5.6 [Error response](#56-error-response) <br>
   5.7 [SwaggerUI](#57-swagger-ui) <br>
6. [Planned features](#6-planned-features) <br>
7. [Attribution and citation](#7-licensing-attribution-and-citation) <br>
8. [Acknowledgments](#8-acknowledgments) <br>
9. [Reference sources](#9-reference-sources) <br>

## 1. TECH AND DEPENDENCIES

<strong>Indoor-analytics</strong> is developed using the following technologies: <br>

Core: <br>
![image](https://img.shields.io/badge/17-Java-orange?style=for-the-badge) &nbsp;
![image](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot) &nbsp;

CI/CD:<br>
![image](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)
&nbsp;
![image](https://img.shields.io/badge/Sonar%20cloud-F3702A?style=for-the-badge&logo=sonarcloud&logoColor=white) &nbsp;

Engineering:<br>
[![Unitility](https://img.shields.io/badge/UNITILITY-2.3.0-13ADF3?style=for-the-badge&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyMi41bW0iIGhlaWdodD0iMTQuNW1tIiB2aWV3Qm94PSIwIDAgMjI1MCAxNDUwIj4NCiAgPHBvbHlnb24gZmlsbD0iIzUwN0QxNCIgcG9pbnRzPSIyMjQxLjAzLDE1Ljg4IDExMzYuMzgsMTUuODQgOTA1Ljg4LDQxNS4xIDIwMTAuNTMsNDE1LjA5IiAvPg0KICA8cG9seWdvbiBmaWxsPSIjNzFBQjIzIiBwb2ludHM9IjExMTYuMzgsMTUuODQgNjU1Ljk5LDE1Ljg0IDQ5NC4xNSwyOTYuMTcgNzI4LjM1LDY5NC44OCIgLz4NCiAgPHBvbHlnb24gZmlsbD0iIzhBQzkzNCIgcG9pbnRzPSI0ODQuMTUsMzA2LjE3IDI1NS4wNiw3MDIuOTYgMzg3LjY2LDkzMi42NCA4NDUuODMsOTMyLjYzIiAvPg0KICA8cG9seWdvbiBmaWxsPSIjNThEMEZGIiBwb2ludHM9Ii03LjE3LDE0NDAuMDkgMTA5Ny45NywxNDQwLjA4IDEzMjguNDcsMTA0MC44MyAyMjMuMzIsMTA0MC44NSIgLz4NCiAgPHBvbHlnb24gZmlsbD0iIzEzQURGMyIgcG9pbnRzPSIxNzM5LjA0LDExNjAuOTEgMTUwOS4wOSw3NjIuNjQgMTExNy45NywxNDQwLjA4IDExODYuOTMsMTQ0MC4wOCAxNTc3Ljg3LDE0NDAuMDgiIC8+DQogIDxwb2x5Z29uIGZpbGw9IiMwMzkzRDAiIHBvaW50cz0iMTk3OC44LDc1Mi45NiAxODQ2LjIsNTIzLjMgMTM4Ni42OCw1MjMuMyAxNzQ5LjA0LDExNTAuOTEiIC8+DQo8L3N2Zz4=)](https://github.com/pjazdzyk/Unitility) &nbsp;
[![Brent-Dekker-Solver](https://img.shields.io/badge/Brent_Dekker%20solver-1.1.4-13ADF3?style=for-the-badge&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyMi41bW0iIGhlaWdodD0iMTQuNW1tIiB2aWV3Qm94PSIwIDAgMjI1MCAxNDUwIj4NCiAgPHBvbHlnb24gZmlsbD0iIzUwN0QxNCIgcG9pbnRzPSIyMjQxLjAzLDE1Ljg4IDExMzYuMzgsMTUuODQgOTA1Ljg4LDQxNS4xIDIwMTAuNTMsNDE1LjA5IiAvPg0KICA8cG9seWdvbiBmaWxsPSIjNzFBQjIzIiBwb2ludHM9IjExMTYuMzgsMTUuODQgNjU1Ljk5LDE1Ljg0IDQ5NC4xNSwyOTYuMTcgNzI4LjM1LDY5NC44OCIgLz4NCiAgPHBvbHlnb24gZmlsbD0iIzhBQzkzNCIgcG9pbnRzPSI0ODQuMTUsMzA2LjE3IDI1NS4wNiw3MDIuOTYgMzg3LjY2LDkzMi42NCA4NDUuODMsOTMyLjYzIiAvPg0KICA8cG9seWdvbiBmaWxsPSIjNThEMEZGIiBwb2ludHM9Ii03LjE3LDE0NDAuMDkgMTA5Ny45NywxNDQwLjA4IDEzMjguNDcsMTA0MC44MyAyMjMuMzIsMTA0MC44NSIgLz4NCiAgPHBvbHlnb24gZmlsbD0iIzEzQURGMyIgcG9pbnRzPSIxNzM5LjA0LDExNjAuOTEgMTUwOS4wOSw3NjIuNjQgMTExNy45NywxNDQwLjA4IDExODYuOTMsMTQ0MC4wOCAxNTc3Ljg3LDE0NDAuMDgiIC8+DQogIDxwb2x5Z29uIGZpbGw9IiMwMzkzRDAiIHBvaW50cz0iMTk3OC44LDc1Mi45NiAxODQ2LjIsNTIzLjMgMTM4Ni42OCw1MjMuMyAxNzQ5LjA0LDExNTAuOTEiIC8+DQo8L3N2Zz4=)](https://github.com/pjazdzyk/brent-dekker-solver) &nbsp;
[![Hvac-Engine](https://img.shields.io/badge/Hvac_Engine-1.2.0-13ADF3?style=for-the-badge&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyMi41bW0iIGhlaWdodD0iMTQuNW1tIiB2aWV3Qm94PSIwIDAgMjI1MCAxNDUwIj4NCiAgPHBvbHlnb24gZmlsbD0iIzUwN0QxNCIgcG9pbnRzPSIyMjQxLjAzLDE1Ljg4IDExMzYuMzgsMTUuODQgOTA1Ljg4LDQxNS4xIDIwMTAuNTMsNDE1LjA5IiAvPg0KICA8cG9seWdvbiBmaWxsPSIjNzFBQjIzIiBwb2ludHM9IjExMTYuMzgsMTUuODQgNjU1Ljk5LDE1Ljg0IDQ5NC4xNSwyOTYuMTcgNzI4LjM1LDY5NC44OCIgLz4NCiAgPHBvbHlnb24gZmlsbD0iIzhBQzkzNCIgcG9pbnRzPSI0ODQuMTUsMzA2LjE3IDI1NS4wNiw3MDIuOTYgMzg3LjY2LDkzMi42NCA4NDUuODMsOTMyLjYzIiAvPg0KICA8cG9seWdvbiBmaWxsPSIjNThEMEZGIiBwb2ludHM9Ii03LjE3LDE0NDAuMDkgMTA5Ny45NywxNDQwLjA4IDEzMjguNDcsMTA0MC44MyAyMjMuMzIsMTA0MC44NSIgLz4NCiAgPHBvbHlnb24gZmlsbD0iIzEzQURGMyIgcG9pbnRzPSIxNzM5LjA0LDExNjAuOTEgMTUwOS4wOSw3NjIuNjQgMTExNy45NywxNDQwLjA4IDExODYuOTMsMTQ0MC4wOCAxNTc3Ljg3LDE0NDAuMDgiIC8+DQogIDxwb2x5Z29uIGZpbGw9IiMwMzkzRDAiIHBvaW50cz0iMTk3OC44LDc1Mi45NiAxODQ2LjIsNTIzLjMgMTM4Ni42OCw1MjMuMyAxNzQ5LjA0LDExNTAuOTEiIC8+DQo8L3N2Zz4=)](https://github.com/pjazdzyk/hvac-engine) &nbsp;

## 2. ARCHITECTURE
Indoor-Analytics is designed as monolith in hexagonal architecture. Why hexagonal? For practice and training, and because I like it.
Project is modular with separated API and CORE Maven modules. API module is composed of port interfaces to be implemented
by Rest Controllers in CORE infrastructure. CORE also includes domain package, framework-less which can be easily extracted
to any other project and framework.

## 3. CURRENT VERSION
Status: pre-release <br>
Version: **0.0.1-alpha**

## 4. FUNCTIONALITY
At the current state of development, Indoor-Analytics is available offline and has not yet been published. 
For testing purposes, you can run it locally or in a Docker environment (simply launch 'build_image.bat') to build your 
image and start your container, exposing port 8090. A fully described SwaggerUI is provided for testing the REST API. 
The frontend is currently in preparation; once ready for testing, it will be published on a cloud provider along with the REST API.
Current application capabilities can be described as below:

**Dry air properties:**
* relative humidity,
* kinematic and dynamic viscosity,
* thermal conductivity,
* specific enthalpy,
* specific heat,
* density,
* thermal diffusivity,
* Prandtl number,

**Moist air properties:**
* vapour saturation pressure,
* dew point temperature, wet bulb temperature,
* relative humidity, 
* humidity ratio and maximum humidity ratio,
* kinematic and dynamic viscosity,
* thermal conductivity,
* specific enthalpy of humid air with water mist and ice mist components,
* specific heat,
* density,
* thermal diffusivity,
* Prandtl number,

**Air heating:**
* heating process for input heating power,
* heating process for target outlet air temperature,
* heating process for target outlet air relative humidity,

**Air cooling:**
* real cooling process with condensate discharge process for input cooling power,
* real cooling process with condensate discharge process for target outlet temperature,
* real cooling process with condensate discharge process for target outlet relative humidity,

**Air stream mixing:**
* simple mixing of two flows with humidity content,
* mixing of multiple flows with humidity content,

NOTE: It is planned that properties of both dry and humid air, along with a sample heating process, will be provided free of charge. 
However, access to the rest of the thermodynamic processes will be limited to a few uses per month for unregistered users. 
For registered users, additional usage will be available for a small fee. The REST API will be accessible to registered 
users with limited calls per month in the free-tier plan. A greater number of calls will incur a small fee to cover 
hosting and cloud provider expenses.

## 5. REST API

Indoor-analytics REST Api is dedicated for developers who would like to use capabilities of this service for developing 
their own HVAC software systems.

### 5.1. Versioning
Versioning is not planned for simplicity and to avoid maintain of multiple apis and versions at the same time. However, if
you will need it for any reason contact me.

### 5.2. Physical properties of humid air

Endpoints available for users are listed below. Parameters written in bold font are required, rest are optional. If not 
specified - default values will be assumed: <br>
- pressure: "101325.0Pa"
- relative-humidity: "0.0%"
- humidity ratio: "0.0kg/kg"
- imperial-units: "false"

Physical quantities must be specified as String type with value and associated quantity unit, for an example: "20.5C"
as 20.5 degrees of Celsius. For list of supported units, see the [Unitility](https://github.com/pjazdzyk/unitility) user guide. <br>
Imperial-units determines the unit system used in the response. For the input in query param or request objects you can use
any unit you want from the supported units pool (both imperial and SI).

| LP | PATH                                   | MTHD | QUERY PARAMS                                                                             |
|----|----------------------------------------|--------|------------------------------------------------------------------------------------------|
| 1  | `/properties/dry-air`                  | GET    | **temperature**<br/>pressure<br/>imperial-units                                          |
| 2  | `/properties/humid-air`                | GET    | **temperature**<br/>pressure<br/>humidity-ratio<br/>relative-humidity<br/>imperial-units |
| 3  | `/properties/humid-air/from-wet-bulb`  | GET    | **wet-bulb-temperature**<br/>pressure<br/>relative-humidity<br/>imperial-units           |
| 4  | `/properties/humid-air/from-dew-point` | GET    | **dew-point-temperature**<br/>pressure<br/>relative-humidity<br/>imperial-units          |
| 5  | `/properties/humid-air/from-enthalpy`  | GET    | **specific-enthalpy**<br/>pressure<br/>**humidity-ratio**<br/>imperial-units             |
| 6  | `/properties/humid-air/from-humidity`  | GET    | **humidity-ratio**<br/>**relative-humidity**<br/>pressure<br/>imperial-units             |

Humid air response example in SI units: [humid_air_response_SI.json](examples%2Fhumid_air_response_SI.json) <br>
Dry air response example in imperial units: [dry_air_response_imperial.json](examples%2Fdry_air_response_imperial.json) <br>

### 5.3. Process of heating
Process of heating ias available in 3 different modes: from input power, for target temperature or for target relative humidity.
More details on heating process can be found in [HVAC|Engine](https://github.com/pjazdzyk/hvac-engine/blob/master/README_GUIDE.MD) 
library user guide, section 3.1 Heating.

| LP | PATH                                          | MTHD | REQUEST BODY EXAMPLE                                                | QUERY PARAMS  |
|----|-----------------------------------------------|--------|---------------------------------------------------------------------|---------------|
| 1  | `/processes/heating/input-power`              | POST   | [request-body-example](examples%2Fheating_power_request.json)     | imperial-units |
| 2  | `/processes/heating/target-temperature`       | POST   | [request-body-example](examples%2Fheating_temperature_request.json) | imperial-units |
| 3  | `/processes/heating/target-relative-humidity` | POST   | [request-body-example](examples%2Fheating_humidity_request.json)    | imperial-units |

As previously explained, setting query param imperial-units to true, will provide calculation result in a predefined set 
of imperial units.<br>
Heating response example in SI units: [heating_response_SI.json](examples%2Fheating_response_SI.json) <br>
Heating response example in imperial units: [heating_response_imperial.json](examples%2Fheating_response_imperial.json) <br>

### 5.4. Process of real cooling with condensate discharge
Process of cooling ias available in 3 different modes: from input power, for target temperature or for target relative humidity.
More details on heating process can be found in [HVAC|Engine](https://github.com/pjazdzyk/hvac-engine/blob/master/README_GUIDE.MD)
library user guide, section 3.2 Cooling.

| LP | PATH                                          | MTHD | REQUEST BODY EXAMPLE                    | QUERY PARAMS   |
|----|-----------------------------------------------|--------|-----------------------------------------|----------------|
| 1  | `/processes/cooling/input-power`              | POST   | [request-body-example](examples%2Fcooling_power_request.json)       | imperial-units |
| 2  | `/processes/cooling/target-temperature`       | POST   | [request-body-example](examples%2Fcooling_temperature_request.json) | imperial-units |
| 3  | `/processes/cooling/target-relative-humidity` | POST   | [request-body-example](examples%2Fcooling_humidity_request.json)    | imperial-units |

Important: Please keep in mind that input cooling power mus be specified with negative sign. And consequently, cooling power
in the resulting response will also be provided as negative value. Response contains also detailed data on water condensate 
discharge.<br>

Cooling response example in SI units: [cooling_response_SI.json](examples%2Fcooling_response_SI.json) <br>
Cooling response example in imperial units: [cooling_response_imperial.json](examples%2Fcooling_response_imperial.json) <br>

### 5.5. Process of mixing
Process of mixing ias available in 2 different modes: mixing of two humid air flows and mixing of multiple humid air flows, up to 20.
More details on heating process can be found in [HVAC|Engine](https://github.com/pjazdzyk/hvac-engine/blob/master/README_GUIDE.MD)
library user guide, section 3.3 Mixing.

| LP | PATH                         | MTHD | REQUEST BODY EXAMPLE                | QUERY PARAMS   |
|----|------------------------------|------|-------------------------------------|----------------|
| 1  | `/processes/mixing`          | POST | [request-body-example](examples%2Fmixing_simple_request.json)   | imperial-units |
| 2  | `/processes/mixing/multiple` | POST | [request-body-example](examples%2Fmixing_multiple_request.json) | imperial-units |

Mixing response example in SI units: [mixing_response_SI.json](examples%2Fmixing_response_SI.json) <br>
Mixing response example in imperial units: [mixing_response_imperial.json](examples%2Fmixing_response_imperial.json) <br>

### 5.6. Error response
In case of validation errors or domain exceptions response will result in HTTP code of 400 (Bad Request).
[InvalidResponse](api%2Fsrc%2Fmain%2Fjava%2Fcom%2Fsynerset%2Findooranalytics%2Fcommon%2FInvalidResponse.java) will
be created and returned to user, with following structure:
```json
{
  "serviceName": "Indoor Analytics",
  "cause": "UnitSystemParseException",
  "message": "Unsupported unit symbol: {xyz}. Target class: TemperatureUnits",
  "timestamp": "2024-02-10T14:39:11.8551038Z"
}
```
Exception stack trace should never be returned to the user. If this happens, please let me know as soon as possible.

### 5.7. Swagger Ui
Swagger url will be provided here after first version of service will be uploaded to cloud provider for testing.

## 6. PLANNED FEATURES
- steam humidification process,
- water spray humidification process,
- heat recovery process,
- process execution engine - so you could send a process list and engine will execute one after another,
taking output air of the previous process as an input to another process, to simulate multi-stage change of air state
as in real HVAC device.
- tables generator of physical properties in CSV or EXCEL,
- GUI to make engineers life easier,
- User registration, payment gate

Is there any feature you urgently need for your engineering application? Let me know what is needed and I see
what I can do.

## 7. LICENSING, ATTRIBUTION, AND CITATION
Please be informed that any reference to this project must be appropriately cited to include the author's attribution. 
Additionally, it should be noted that while samples may have been shared publicly for educational purposes, the project
as a whole is designated for commercial utilization. Unauthorized commercial usage is strictly prohibited. 
All rights are reserved by the author.

Tech shield with version tag for manual adjustment (you can indicate which version you actually use): <br>
[![Indoor Analytics](https://img.shields.io/badge/Indoor_Analytics-v0.0.1-13ADF3?style=for-the-badge&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyMi41bW0iIGhlaWdodD0iMTQuNW1tIiB2aWV3Qm94PSIwIDAgMjI1MCAxNDUwIj4NCiAgPHBvbHlnb24gZmlsbD0iIzUwN0QxNCIgcG9pbnRzPSIyMjQxLjAzLDE1Ljg4IDExMzYuMzgsMTUuODQgOTA1Ljg4LDQxNS4xIDIwMTAuNTMsNDE1LjA5IiAvPg0KICA8cG9seWdvbiBmaWxsPSIjNzFBQjIzIiBwb2ludHM9IjExMTYuMzgsMTUuODQgNjU1Ljk5LDE1Ljg0IDQ5NC4xNSwyOTYuMTcgNzI4LjM1LDY5NC44OCIgLz4NCiAgPHBvbHlnb24gZmlsbD0iIzhBQzkzNCIgcG9pbnRzPSI0ODQuMTUsMzA2LjE3IDI1NS4wNiw3MDIuOTYgMzg3LjY2LDkzMi42NCA4NDUuODMsOTMyLjYzIiAvPg0KICA8cG9seWdvbiBmaWxsPSIjNThEMEZGIiBwb2ludHM9Ii03LjE3LDE0NDAuMDkgMTA5Ny45NywxNDQwLjA4IDEzMjguNDcsMTA0MC44MyAyMjMuMzIsMTA0MC44NSIgLz4NCiAgPHBvbHlnb24gZmlsbD0iIzEzQURGMyIgcG9pbnRzPSIxNzM5LjA0LDExNjAuOTEgMTUwOS4wOSw3NjIuNjQgMTExNy45NywxNDQwLjA4IDExODYuOTMsMTQ0MC4wOCAxNTc3Ljg3LDE0NDAuMDgiIC8+DQogIDxwb2x5Z29uIGZpbGw9IiMwMzkzRDAiIHBvaW50cz0iMTk3OC44LDc1Mi45NiAxODQ2LjIsNTIzLjMgMTM4Ni42OCw1MjMuMyAxNzQ5LjA0LDExNTAuOTEiIC8+DQo8L3N2Zz4=)](https://github.com/pjazdzyk/indoor-analytics)

```markdown
[![Indoor Analytics](https://img.shields.io/badge/Indoor_Analytics-v0.0.1-13ADF3?style=for-the-badge&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyMi41bW0iIGhlaWdodD0iMTQuNW1tIiB2aWV3Qm94PSIwIDAgMjI1MCAxNDUwIj4NCiAgPHBvbHlnb24gZmlsbD0iIzUwN0QxNCIgcG9pbnRzPSIyMjQxLjAzLDE1Ljg4IDExMzYuMzgsMTUuODQgOTA1Ljg4LDQxNS4xIDIwMTAuNTMsNDE1LjA5IiAvPg0KICA8cG9seWdvbiBmaWxsPSIjNzFBQjIzIiBwb2ludHM9IjExMTYuMzgsMTUuODQgNjU1Ljk5LDE1Ljg0IDQ5NC4xNSwyOTYuMTcgNzI4LjM1LDY5NC44OCIgLz4NCiAgPHBvbHlnb24gZmlsbD0iIzhBQzkzNCIgcG9pbnRzPSI0ODQuMTUsMzA2LjE3IDI1NS4wNiw3MDIuOTYgMzg3LjY2LDkzMi42NCA4NDUuODMsOTMyLjYzIiAvPg0KICA8cG9seWdvbiBmaWxsPSIjNThEMEZGIiBwb2ludHM9Ii03LjE3LDE0NDAuMDkgMTA5Ny45NywxNDQwLjA4IDEzMjguNDcsMTA0MC44MyAyMjMuMzIsMTA0MC44NSIgLz4NCiAgPHBvbHlnb24gZmlsbD0iIzEzQURGMyIgcG9pbnRzPSIxNzM5LjA0LDExNjAuOTEgMTUwOS4wOSw3NjIuNjQgMTExNy45NywxNDQwLjA4IDExODYuOTMsMTQ0MC4wOCAxNTc3Ljg3LDE0NDAuMDgiIC8+DQogIDxwb2x5Z29uIGZpbGw9IiMwMzkzRDAiIHBvaW50cz0iMTk3OC44LDc1Mi45NiAxODQ2LjIsNTIzLjMgMTM4Ni42OCw1MjMuMyAxNzQ5LjA0LDExNTAuOTEiIC8+DQo8L3N2Zz4=)](https://github.com/pjazdzyk/indoor-analytics)
```

## 8. ACKNOWLEDGMENTS

I extend my heartfelt gratitude to the [Silesian University of Technology](https://www.polsl.pl/en/) for imparting
invaluable knowledge to me.<br>

## 9. REFERENCE SOURCES
In the JavaDoc, you'll find linked references to specific papers and technical literature listed below.

* [1] - ASHRAE FUNDAMENTALS 2002, CHAPTER 6 "Psychrometrics"
* [2] - Buck, Arden L. "New Equations for Computing Vapour Pressure and Enhancement Factor". Journal of Applied Meteorology and Climatology (December 1981).
* [3] - Buck Research Instruments L.L.C. "MODEL CR-1A HYGROMETER WITH AUTO FILL OPERATING MANUAL" (May 2012).
* [4] - Morvay Z.K, Gvozdenac D.D. "Fundamentals for analysis and calculation of energy and environmental performance". Applied Industrial Energy And Environmental Management.
* [5] - Lipska B. "Projektowanie Wentylacji i Klimatyzacji. Podstawy uzdatniania powietrza" Wydawnictwo Politechniki Śląskiej (Gliwice 2014).
* [6] - https://www.engineeringtoolbox.com
* [7] - Stull R. "Wet-Bulb Temperature from Relative Humidity and Air Temperature". Manuscript received 14 July 2011, in final form 28 August 2011
* [8] - Tsilingiris P.T "Thermophysical and transport properties of humid air at temperature range between 0 and 100oC". Elsevier, Science Direct (September 2007)
* [9] - E.W. Lemmon, R.T. Jacobsen, S.G. Penoncello, D. Friend. Thermodynamic Properties of Air and Mixtures of Nitrogen, Argon, and Oxygen from 60 to 2000 K at Pressures to 2000 MPa. J. Phys. Chem. Ref. Data, Vol. 29, No. 3, (2000)
* [11] - F.E. Jones, G.L. Harris. ITS-90 Density of water formulation for volumetric standards' calibration. Journal of Research of the National Institute of Standards and Technology (1992)
* [12] - Water specific heat tables: https://www.engineeringtoolbox.com/specific-heat-capacity-water-d_660.html