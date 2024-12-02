package com.yourstechnology.form.handler;

import java.util.Locale;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.yourstechnology.form.exception.CustomUnauthorizedException;
import com.yourstechnology.form.exception.DuplicateResourceException;
import com.yourstechnology.form.exception.ResourceNotFoundException;
import com.yourstechnology.form.utils.dto.ResponseDto;

@RestControllerAdvice
public class ExceptionResolver {

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseDto<Boolean> handleNoHandlerFound(NoHandlerFoundException e, WebRequest request, Locale locale) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		response.setData(false);
		response.setMessage(messageSource.getMessage("errors.NoHandlerFoundException", null, locale));
		return response;
	}

	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseDto<Boolean> noSuchElementException(NoSuchElementException exc, Locale locale) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		Object[] args = {
				exc.getMessage()
		};
		response.setMessage(messageSource.getMessage("errors.NoSuchElementException", args, locale));
		return response;
	}

	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseDto<Boolean> nullPointerException(NullPointerException exc, Locale locale) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		response.setMessage(exc.getLocalizedMessage());
		return response;
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseDto<Boolean> illegalArgumentException(IllegalArgumentException iae, Locale locale) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		response.setMessage(messageSource.getMessage("errors.IllegalArgumentException", null, locale));
		return response;
	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseDto<Boolean> notFoundException(NotFoundException nfe, Locale locale) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		response.setMessage(messageSource.getMessage("errors.IllegalArgumentException", null, locale));
		return response;
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseDto<Boolean> resouceNotFoundException(ResourceNotFoundException nfe, Locale locale) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		Object[] args = {
				nfe.getMessage()
		};
		response.setMessage(messageSource.getMessage("errors.NoSuchElementException", args, locale));
		return response;
	}

	@ExceptionHandler(DuplicateResourceException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseDto<Boolean> duplicateResourceException(DuplicateResourceException dre, Locale locale) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		Object[] args = {
				dre.getMessage()
		};
		response.setMessage(messageSource.getMessage("errors.DuplicateResourceException", args, locale));
		return response;
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseDto<Boolean> dataIntegrityViolationException(DataIntegrityViolationException dre) {
		// String msg = dre.getMessage();
		// if (dre.getCause().getCause() instanceof SQLException) {
		// SQLException e = (SQLException) dre.getCause().getCause();
		// msg = e.getLocalizedMessage();
		// }
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		response.setMessage(dre.getLocalizedMessage());
		return response;
	}

	@ExceptionHandler(CustomUnauthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseDto<Boolean> customUnauthorizedException(CustomUnauthorizedException cue, Locale locale) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		response.setMessage(messageSource.getMessage("errors.CustomUnauthorizedException", null, locale));
		return response;
	}

	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseDto<Boolean> invalidCredentialsException(BadCredentialsException exc, Locale locale){
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setMessage(messageSource.getMessage("errors.InternalAuthenticationServiceException", null, locale));
		return response;
	}

	@ExceptionHandler(InternalAuthenticationServiceException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseDto<Boolean> internalAuthenticationServiceException(InternalAuthenticationServiceException exc, Locale locale){
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setMessage(messageSource.getMessage("errors.InternalAuthenticationServiceException", null, locale));
		return response;
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseDto<Boolean> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException hrmnse) {
		ResponseDto<Boolean> response = new ResponseDto<>();
		response.setData(false);
		response.setMessage(hrmnse.getDetailMessageCode());
		return response;
	}
}
