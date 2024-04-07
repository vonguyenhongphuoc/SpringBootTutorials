package com.devhp.SpringRestDemoWithGradle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import com.devhp.SpringRestDemoWithGradle.config.RsaKeyProperties;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
// @ComponentScan({"com.delivery.request"})
// @EntityScan("com.delivery.domain")
@ComponentScan
// @EntityScan
@SecurityScheme(name = "devhp-demo-api", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class SpringRestDemoWithGradleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestDemoWithGradleApplication.class, args);
	}

}
