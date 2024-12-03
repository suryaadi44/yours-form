package com.yourstechnology.form.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.yourstechnology.form.config.applicationConfig.ApplicationConfiguration;
import com.yourstechnology.form.features.auth.user.UserRepository;
import com.yourstechnology.form.handler.AccessDeniedHandlerFilter;
import com.yourstechnology.form.handler.AuthenticationEntryPointException;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
	@Autowired
	private ApplicationConfiguration applicationConfiguration;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	private UserRepository userRepo;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		RequestMatcher csrfRequestMatcher = new RequestMatcher() {
			private AntPathRequestMatcher[] disableCsrfMatchers = {
					new AntPathRequestMatcher("/**")
			};

			@Override
			public boolean matches(HttpServletRequest request) {

				for (AntPathRequestMatcher rm : disableCsrfMatchers) {
					if (rm.matches(request)) {
						return false;
					}
				}
				return true;
			}
		};
		http.cors(withDefaults()).csrf(csrf -> csrf.requireCsrfProtectionMatcher(csrfRequestMatcher));
		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.authorizeHttpRequests(
				requests -> requests.requestMatchers(getWhiteList()).permitAll().anyRequest().authenticated());
		http.exceptionHandling(handling -> handling.accessDeniedHandler(new AccessDeniedHandlerFilter()));
		http.exceptionHandling(handling -> handling.authenticationEntryPoint(new AuthenticationEntryPointException()));
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(applicationConfiguration.getAllowedOrigins());
		configuration.setAllowCredentials(true);
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type"));
		configuration.setExposedHeaders(Arrays.asList("trace-id"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	private String[] getWhiteList() {
		String[] allowedCorsString = {
				"/swagger-ui.html", "/actuator/**", "/v3/api-docs/**", "/swagger-ui/**", "/api/v1/auth/login",
				"/api/v1/auth/register", "/api/v1/auth/refresh-token",
		};
		return allowedCorsString;
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return username -> userRepo.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
