package com.yourstechnology.form.features.auth.user.dto;

import lombok.Data;

@Data
public class SignUpRequest {
	private String name;

    private String email;

    private String password;
}
