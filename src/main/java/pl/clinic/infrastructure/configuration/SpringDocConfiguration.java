package pl.clinic.infrastructure.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.clinic.DemoApplication;

@Configuration
public class SpringDocConfiguration {

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
            .group("default")
            .pathsToMatch("/**")
            .packagesToScan(DemoApplication.class.getPackageName())
            .build();
    }

    @Bean
    public OpenAPI springDocOpenApi() {
        return new OpenAPI()
            .components(new Components())
            .info(new Info()
                .title("Clinic application")
                .contact(contact())
                .version("1.0"));
    }

    private Contact contact() {
        return new Contact()
            .name("Clinic")
            .url("not available")
            .email("dawideq195@gmail.com");
    }
}
