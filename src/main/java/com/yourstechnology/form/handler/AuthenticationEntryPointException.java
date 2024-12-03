package com.yourstechnology.form.handler;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourstechnology.form.utils.dto.ResponseDto;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationEntryPointException implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		ResponseDto<Boolean> result = new ResponseDto<>();
		result.setData(false);
		result.setMessage(authException.getLocalizedMessage());

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(response.getOutputStream(), result);
	}
}