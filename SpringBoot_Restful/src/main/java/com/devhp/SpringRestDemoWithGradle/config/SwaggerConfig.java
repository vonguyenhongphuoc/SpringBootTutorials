package com.devhp.SpringRestDemoWithGradle.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Demo API", version = "Version 1.0", contact = @Contact(name = "DEV HP", email = "devhp@gmail.com", url = "https://google.com/"), license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"), termsOfService = "https://youtube.com/", description = "Spring Boot RestFul API Demo by HP"))
public class SwaggerConfig {

}
