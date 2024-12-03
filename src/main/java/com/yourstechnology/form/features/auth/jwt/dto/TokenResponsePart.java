package com.yourstechnology.form.features.auth.jwt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponsePart {
    private String token;
    private Long expiresAt;
}
