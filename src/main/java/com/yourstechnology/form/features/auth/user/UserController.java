package com.yourstechnology.form.features.auth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yourstechnology.form.features.auth.user.dto.AuthRequest;
import com.yourstechnology.form.features.auth.user.dto.AuthResponse;
import com.yourstechnology.form.features.auth.user.dto.SignUpRequest;
import com.yourstechnology.form.features.auth.user.dto.UserResponse;
import com.yourstechnology.form.utils.dto.CommonCreatedResponse;
import com.yourstechnology.form.utils.dto.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "auth.user")
public class UserController {
	@Autowired
	private UserService mainService;

	@GetMapping("/me")
	@Operation(summary = "Get current logged in user data", security = {
			@SecurityRequirement(name = "bearerToken")
	})
	public ResponseEntity<ResponseDto<UserResponse>> getLoggedUser() {
		UserResponse result = mainService.getLoggedUser();
		ResponseDto<UserResponse> response = new ResponseDto<>();
		response.setMessage(mainService.FEATURE_NAME);
		response.setData(result);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseDto<AuthResponse>> login(@RequestBody @Valid AuthRequest req) {
		AuthResponse result = mainService.login(req);

		ResponseDto<AuthResponse> response = new ResponseDto<>();
		response.setMessage(mainService.FEATURE_NAME);
		response.setData(result);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping("/register")
	public ResponseEntity<ResponseDto<CommonCreatedResponse>> signup(@RequestBody @Valid SignUpRequest request) {
		CommonCreatedResponse result = mainService.signup(request);

		ResponseDto<CommonCreatedResponse> response = new ResponseDto<>();
		response.setMessage(mainService.FEATURE_NAME);
		response.setData(result);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

}
