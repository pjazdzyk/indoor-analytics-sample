package com.synerset.indooranalyticsapi.common;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * This interface represents the root of the Swagger Indoor Analytics Rest Service.
 * It defines metadata such as title, version, contact information, and external documentation.
 */
@OpenAPIDefinition(
        info = @Info(title = "SYNERSET - Indoor Analytics Rest Service", version = "0.0.1-alpha",
        contact = @Contact(name = "Author: Piotr Jazdzyk, MSc Eng", url = "https://www.linkedin.com/in/pjazdzyk")),
        externalDocs = @ExternalDocumentation(description = "Powered by: HVAC|Engine - HVAC engineering library",
                url = "https://github.com/pjazdzyk/hvac-engine"),
        tags = {
                @Tag(name = "Physical properties service:"),
                @Tag(name = "Heating process service:")
        })
public interface SwaggerApiRoot {
}