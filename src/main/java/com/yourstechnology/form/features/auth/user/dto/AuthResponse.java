package com.yourstechnology.form.features.auth.user.dto;

import java.util.UUID;

import com.yourstechnology.form.features.auth.jwt.dto.TokenResponsePart;

import lombok.Data;

@Data
public class AuthResponse {
    private UUID id;
    private TokenResponsePart access;
    private TokenResponsePart refresh;
}
