package com.yourstechnology.form.features.auth.user.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class GetUserResponse {
    private UUID id;

    private String name;

    private String username;

    private OffsetDateTime emailVerifiedAt;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
