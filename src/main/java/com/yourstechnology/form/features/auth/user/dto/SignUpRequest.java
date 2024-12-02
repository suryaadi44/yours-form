package com.yourstechnology.form.features.auth.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {
	@NotBlank
	private String name;

	@NotBlank
	@Email
    private String email;

	@NotBlank
	@Size(min = 6, max = 20)
    private String password;
}
