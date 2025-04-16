package com.ylvaldes.telegram_reader.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author YasmaniLedesmaValdez
 * @project telegram-reader
 * @package com.ylvaldes.telegram_reader.service
 * @created 22/4/2023
 * @implNote
 */
@Configuration
public class OpenAPIConfiguration {
    @Bean
    public OpenAPI configServerOpenAPI(@Value("${info.project.version}")
                                       String version, @Value("${info.project.name}")
                                       String name, @Value("${info.project.description}")
                                       String description, @Value("${info.project.organization}")
                                       String organizationName, @Value("${info.project.url}")
                                       String organizationUrl) {
        return new OpenAPI()
                .info(new Info().title("API Documentation " + name)
                        .version(version)
                        .description(description)
                        .contact(new Contact().name(organizationName).url(organizationUrl))
                );
    }
}
