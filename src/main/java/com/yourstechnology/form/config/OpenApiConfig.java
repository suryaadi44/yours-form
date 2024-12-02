package com.yourstechnology.form.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yourstechnology.form.config.applicationConfig.ApplicationConfiguration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {
	@Autowired
	private ApplicationConfiguration applicationConfiguration;

	@Bean
	public OpenAPI springBootOpenAPI() {
		return new OpenAPI()
				.components(new Components().addSecuritySchemes("bearerToken",
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("Bearer").bearerFormat("JWT")))
				.info(new Info().title(applicationConfiguration.getName()).description("Your's Technology Form Api")
						.version(applicationConfiguration.getVersion())
						.license(new License().name("Apache 2.0").url("http://springdoc.org")));
	}
}
