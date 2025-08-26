package com.example.SalesRecordManagement.Swagger;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sales Record Management - Gopii Krisshna M")
                        .version("1.0")
                        .description("This is the API documentation for my application"));
    }
}
