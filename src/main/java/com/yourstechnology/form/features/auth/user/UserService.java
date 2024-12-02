package com.yourstechnology.form.features.auth.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yourstechnology.form.features.auth.jwt.JwtService;
import com.yourstechnology.form.features.auth.user.dto.AuthRequest;
import com.yourstechnology.form.features.auth.user.dto.AuthResponse;
import com.yourstechnology.form.features.auth.user.dto.SignUpRequest;
import com.yourstechnology.form.features.auth.user.dto.UserResponse;
import com.yourstechnology.form.utils.dto.CommonCreatedResponse;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UserService {
	public final String FEATURE_NAME = "User";

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository mainRepo;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private ModelMapper modelMapper;

	public UserResponse getLoggedUser() {
		User userLogin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		UserResponse response = modelMapper.map(userLogin, UserResponse.class);

		return response;
	}

	public AuthResponse login(AuthRequest request) {

		Authentication user = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		AuthResponse response = jwtService.generateTokenPair((User) user.getPrincipal());

		return response;
	}

	public CommonCreatedResponse signup(SignUpRequest request) {
		User user = modelMapper.map(request, User.class);

		user.setPassword(encoder.encode(user.getPassword()));
		user = mainRepo.save(user);

		return CommonCreatedResponse.builder().id(user.getId()).build();
	}

}
