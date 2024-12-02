package com.yourstechnology.form.config.applicationConfig;

import java.time.Duration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenConfiguration {
    private String publicKeyPath;
    private String privateKeyPath;
    private Duration expiration;
}
