package serviceregistration.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI serviceRegistrationProject() {
        return new OpenAPI()
                .info(new Info()
                        .title("Запись на прием")
                        .description("Сервис, позволяющий записаться на прием к врачу")
                        .version("0.1")
                        .license(new License().name("Apache 2.0"))
                        .contact(new Contact().name("Alexey Zotov")
                                .email("vgy7_z@mail.ru")
                                .url("https://github.com/zotov88"))
                );
    }
}
