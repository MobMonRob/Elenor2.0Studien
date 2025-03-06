package de.dhbw.elinor2;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        String authorizationTool = "keycloak";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(authorizationTool))
                .components(new Components()
                        .addSecuritySchemes(authorizationTool,
                                new SecurityScheme()
                                        .name(authorizationTool)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}

