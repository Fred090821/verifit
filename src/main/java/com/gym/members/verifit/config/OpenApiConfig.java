package com.gym.members.verifit.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@OpenAPIDefinition
@Configuration
public class OpenApiConfig {

    @Bean
    public RestTemplate restTemplateBean() {
        return new RestTemplate();
    }

    @Bean
    public OpenAPI openApiInformation() {
        Server localServer =
            new Server().url("http://localhost:8080")
                .description("Localhost Server URL");

        Contact contact = new Contact()
            .email("assifred2005@gmail.com")
            .name("Fred Assi");

        Info info = new Info()
            .contact(contact)
            .description("Spring Boot 3 + Open API 3")
            .summary("Demo of Spring Boot fitness attendance app");

        return new OpenAPI().info(info).addServersItem(localServer);
    }
}