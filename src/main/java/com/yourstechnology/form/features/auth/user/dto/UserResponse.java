package com.yourstechnology.form.features.auth.user.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class UserResponse {
    private UUID id;

    private String name;

    private String email;

    private Instant emailVerifiedAt;

    private Instant createdAt;

    private Instant updatedAt;
}
