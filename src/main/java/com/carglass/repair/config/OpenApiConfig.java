package com.carglass.repair.config;


import com.carglass.repair.exception.ErrorResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSchemas("ErrorResponse", new Schema<ErrorResponse>())
                        .addResponses("NotFound", new ApiResponse()
                                .description("Resource not found")
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema<ErrorResponse>().$ref("#/components/schemas/ErrorResponse")))))
                        .addResponses("BadRequest", new ApiResponse()
                                .description("Invalid Request")
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema<ErrorResponse>().$ref("#/components/schemas/ErrorResponse")))))

                        .addResponses("Conflict", new ApiResponse()
                                .description("Conflict – for example duplicate")
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema<ErrorResponse>().$ref("#/components/schemas/ErrorResponse")))))
                );
    }

}
