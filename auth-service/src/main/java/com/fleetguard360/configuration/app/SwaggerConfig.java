package com.fleetguard360.configuration.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "Fleet Guard 360 - Authentication",
                description = "This microservice handles user authentication for the platform." +
                        " It validates login credentials, generates secure JWT tokens for access to other microservices," +
                        " and returns basic information about the authenticated user, such as username and roles." +
                        " All operations are stateless and do not store sessions in memory, ensuring a secure and scalable authentication flow for API clients.",
                version = "0.0.1"
        ),
        servers = {
                @Server(
                        description = "DEV SERVER",
                        url= ""
                )
        },
        security = @SecurityRequirement(
                name = "security token"
        )
)
@SecurityScheme(
        name = "security token",
        description = "Access Token",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
