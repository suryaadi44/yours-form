package com.yourstechnology.form.features.auth.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {
    @Email
    @NotBlank
    private String username;
    
    @NotBlank
    private String password;
}
