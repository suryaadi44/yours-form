package com.yourstechnology.form.config.applicationConfig;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "application")
@Configuration
public class ApplicationConfiguration {
	private String name;
	private String version;
	private String timezone;
	private List<String> allowedOrigins;

	private TokenConfiguration accessToken;
	private TokenConfiguration refreshToken;
}
