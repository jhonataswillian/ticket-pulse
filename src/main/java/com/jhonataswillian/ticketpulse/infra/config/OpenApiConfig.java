package com.jhonataswillian.ticketpulse.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TicketPulse API")
                        .description("API de vendas de ingressos de alta concorrência com Redis e RabbitMQ.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Jhonatas Willian Nicolete")
                                .email("jhonatas@ticketpulse.com")
                                .url("https://github.com/jhonataswillian/ticket-pulse"))
                );
    }
}
