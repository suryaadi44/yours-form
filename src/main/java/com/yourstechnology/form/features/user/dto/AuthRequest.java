package com.yourstechnology.form.features.user.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
