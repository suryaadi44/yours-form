package com.yourstechnology.form.handler;

import java.io.IOException;
import java.util.Locale;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourstechnology.form.exception.CustomUnauthorizedException;
import com.yourstechnology.form.exception.DuplicateResourceException;
import com.yourstechnology.form.exception.ResourceNotFoundException;
import com.yourstechnology.form.utils.dto.ResponseDto;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ExceptionResolver {

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ResponseDto<Boolean>> handleNoHandlerFound(NoHandlerFoundException e, WebRequest request,
			Locale locale) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		response.setData(false);
		response.setMessage(messageSource.getMessage("errors.NoHandlerFoundException", null, locale));
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ResponseDto<Boolean>> noSuchElementException(NoSuchElementException exc, Locale locale) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		Object[] args = {
				exc.getMessage()
		};
		response.setMessage(messageSource.getMessage("errors.NoSuchElementException", args, locale));
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ResponseDto<Boolean>> nullPointerException(NullPointerException exc, Locale locale) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		response.setMessage(exc.getLocalizedMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ResponseDto<Boolean>> illegalArgumentException(IllegalArgumentException iae, Locale locale) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		response.setMessage(messageSource.getMessage("errors.IllegalArgumentException", null, locale));
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ResponseDto<Boolean>> notFoundException(NotFoundException nfe, Locale locale) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		response.setMessage(messageSource.getMessage("errors.IllegalArgumentException", null, locale));
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ResponseDto<Boolean>> resouceNotFoundException(ResourceNotFoundException nfe, Locale locale) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		Object[] args = {
				nfe.getMessage()
		};
		response.setMessage(messageSource.getMessage("errors.NoSuchElementException", args, locale));
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DuplicateResourceException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ResponseDto<Boolean>> duplicateResourceException(DuplicateResourceException dre,
			Locale locale) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		Object[] args = {
				dre.getMessage()
		};
		response.setMessage(messageSource.getMessage("errors.DuplicateResourceException", args, locale));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ResponseDto<Boolean>> dataIntegrityViolationException(DataIntegrityViolationException dre) {
		// String msg = dre.getMessage();
		// if (dre.getCause().getCause() instanceof SQLException) {
		// SQLException e = (SQLException) dre.getCause().getCause();
		// msg = e.getLocalizedMessage();
		// }
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		response.setMessage(dre.getLocalizedMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(CustomUnauthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ResponseDto<Boolean>> customUnauthorizedException(CustomUnauthorizedException cue,
			Locale locale) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		response.setMessage(messageSource.getMessage("errors.CustomUnauthorizedException", null, locale));
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ResponseDto<Boolean>> invalidCredentialsException(BadCredentialsException exc,
			Locale locale) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setMessage(messageSource.getMessage("errors.InternalAuthenticationServiceException", null, locale));
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(InternalAuthenticationServiceException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ResponseDto<Boolean>> internalAuthenticationServiceException(
			InternalAuthenticationServiceException exc, Locale locale) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setMessage(messageSource.getMessage("errors.InternalAuthenticationServiceException", null, locale));
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ResponseDto<Boolean>> httpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException hrmnse) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		response.setMessage(hrmnse.getDetailMessageCode());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AuthorizationDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<ResponseDto<Boolean>> authorizationDeniedException(AuthorizationDeniedException ade,
			Locale locale) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		response.setMessage(messageSource.getMessage("errors.CustomUnauthorizedException", null, locale));
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleAllExceptions(Exception ex, HttpServletResponse response) throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); 

		ResponseDto<Boolean> result = new ResponseDto<>();
		result.setData(false); 
		result.setMessage("Internal Server Error: " + ex.getMessage());
		log.error("Internal Server Error", ex);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(response.getOutputStream(), result);
	}
}
