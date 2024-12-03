package com.yourstechnology.form.features.auth.jwt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import com.fasterxml.uuid.Generators;
import com.yourstechnology.form.config.applicationConfig.ApplicationConfiguration;
import com.yourstechnology.form.exception.CustomUnauthorizedException;
import com.yourstechnology.form.features.auth.jwt.dto.TokenRequestWrapper;
import com.yourstechnology.form.features.auth.jwt.dto.TokenResponsePart;
import com.yourstechnology.form.features.auth.session.Session;
import com.yourstechnology.form.features.auth.session.SessionRepository;
import com.yourstechnology.form.features.auth.user.User;
import com.yourstechnology.form.features.auth.user.dto.AuthResponse;
import com.yourstechnology.form.utils.constants.TokenTypeConst;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {
	@Autowired
	private ApplicationConfiguration config;

	@Autowired
	private ConfigurableApplicationContext ctx;

	@Autowired
	private SessionRepository sessionRepo;

	private PrivateKey AccessTokenPrivateKey;
	private PublicKey AccessTokenPublicKey;
	private PrivateKey RefreshTokenPrivateKey;
	private PublicKey RefreshTokenPublicKey;

	@PostConstruct
	public void loadKeys() {
		try {
			AccessTokenPrivateKey = loadPrivateKey(config.getAccessToken().getPrivateKeyPath());
			AccessTokenPublicKey = loadPublicKey(config.getAccessToken().getPublicKeyPath());
			RefreshTokenPrivateKey = loadPrivateKey(config.getRefreshToken().getPrivateKeyPath());
			RefreshTokenPublicKey = loadPublicKey(config.getRefreshToken().getPublicKeyPath());
		} catch (Exception e) {
			log.error("Error loading keys", e);
			ctx.close();
		}
	}

	private PublicKey loadPublicKey(String path) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		String keyPEM = new String(Files.readAllBytes(Paths.get(path)));
		keyPEM = keyPEM.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "")
				.replaceAll("\\s+", "");

		byte[] keyBytes = Base64.getDecoder().decode(keyPEM);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PublicKey publicKey = kf.generatePublic(spec);

		return publicKey;
	}

	private PrivateKey loadPrivateKey(String path)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		String keyPEM = new String(Files.readAllBytes(Paths.get(path)));
		keyPEM = keyPEM.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "")
				.replaceAll("\\s+", "");

		byte[] keyBytes = Base64.getDecoder().decode(keyPEM);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(spec);
	}

	public String extractUsername(String token) {
		return extractClaim(token, TokenTypeConst.ACCESS_TOKEN, Claims::getSubject);
	}

	public <T> T extractClaim(String token, String category, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token, category);
		return claimsResolver.apply(claims);
	}

	private TokenResponsePart generateAccessToken(TokenRequestWrapper req) {
		Long expiration = config.getAccessToken().getExpiration().toMillis() + System.currentTimeMillis();
		String token = Jwts.builder().claim("cat", TokenTypeConst.ACCESS_TOKEN).claim("sid", req.getSessionId())
				.subject(req.getUser().getUsername()).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(expiration)).signWith(AccessTokenPrivateKey).compact();

		return TokenResponsePart.builder().token(token).expiresAt(expiration).build();
	}

	private TokenResponsePart generateRefreshToken(TokenRequestWrapper req) {
		Long expiration = config.getAccessToken().getExpiration().toMillis() + System.currentTimeMillis();
		String token = Jwts.builder().claim("cat", TokenTypeConst.REFRESH_TOKEN).claim("sid", req.getSessionId())
				.subject(req.getUser().getUsername()).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(expiration)).signWith(RefreshTokenPrivateKey).compact();

		return TokenResponsePart.builder().token(token).expiresAt(expiration).build();
	}

	public AuthResponse generateTokenPair(User user) {
		UUID sessionId = Generators.timeBasedEpochRandomGenerator().generate();

		AuthResponse response = new AuthResponse();
		response.setId(user.getId());
		response.setAccess(generateAccessToken(TokenRequestWrapper.builder().user(user).sessionId(sessionId).build()));
		response.setRefresh(
				generateRefreshToken(TokenRequestWrapper.builder().user(user).sessionId(sessionId).build()));

		Session session = new Session();
		session.setUser(user);
		session.setSessionId(sessionId);
		session.setExpiredAt(new Date(response.getAccess().getExpiresAt()).toInstant());

		sessionRepo.save(session);

		return response;
	}

	public void invalidateToken(String token) {
		UUID sessionId = UUID.fromString(
				extractClaim(token, TokenTypeConst.ACCESS_TOKEN, claims -> claims.get("sid", String.class)));

		Session session = sessionRepo.findBySessionId(sessionId)
				.orElseThrow(() -> new CustomUnauthorizedException("session not found"));

		sessionRepo.delete(session);
	}

	public Boolean isTokenValid(String token, String category) {
		UUID sessionId = UUID.fromString(extractClaim(token, category, claims -> claims.get("sid", String.class)));

		Session session = sessionRepo.findBySessionId(sessionId)
				.orElseThrow(() -> new CustomUnauthorizedException("session not found"));

		return session.getExpiredAt().isAfter(OffsetDateTime.now().toInstant())
				&& category.equals(extractClaim(token, category, claims -> claims.get("cat", String.class)))
				&& !isTokenExpired(token, category);
	}

	private Boolean isTokenExpired(String token, String category) {
		return extractExpiration(token, category).before(new Date());
	}

	private Date extractExpiration(String token, String category) {
		return extractClaim(token, category, Claims::getExpiration);
	}

	public Claims extractAllClaims(String token, String category) {
		return Jwts.parser()
				.verifyWith(category.equals(TokenTypeConst.ACCESS_TOKEN) ? AccessTokenPublicKey : RefreshTokenPublicKey)
				.build().parseSignedClaims(token).getPayload();
	}

}
