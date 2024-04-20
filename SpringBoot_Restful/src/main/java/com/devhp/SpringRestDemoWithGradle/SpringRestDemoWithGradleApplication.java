package com.devhp.SpringRestDemoWithGradle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.devhp.SpringRestDemoWithGradle.util.constants.Constants;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@ComponentScan
@SecurityScheme(name = Constants.SECURITY_APP_NAME, scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class SpringRestDemoWithGradleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestDemoWithGradleApplication.class, args);
	}



}
