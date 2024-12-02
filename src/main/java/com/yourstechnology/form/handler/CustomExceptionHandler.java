package com.yourstechnology.form.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.yourstechnology.form.utils.dto.ResponseDto;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
	@Autowired
	private MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Map<String, List<String>> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors().forEach((FieldError error) -> {
			String fieldName = error.getField();
			String errorMessage = error.getDefaultMessage();

			try {
				errorMessage = messageSource.getMessage(error.getCode(), error.getArguments(), request.getLocale());
			} catch (NoSuchMessageException e) {
			}

			// Add error messages to the list for each field
			errors.computeIfAbsent(fieldName, key -> new ArrayList<>()).add(errorMessage);
		});

		ResponseDto<Map<String, List<String>>> response = new ResponseDto<>();
		response.setMessage(messageSource.getMessage("errors.validation", null, request.getLocale()));
		response.setData(errors);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}