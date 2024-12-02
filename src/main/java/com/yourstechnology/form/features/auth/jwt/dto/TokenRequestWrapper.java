package com.yourstechnology.form.features.auth.jwt.dto;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRequestWrapper {
    private UUID sessionId;
    private UserDetails user;
}
