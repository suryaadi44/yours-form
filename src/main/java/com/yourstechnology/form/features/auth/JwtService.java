package com.yourstechnology.form.features.auth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.yourstechnology.form.config.applicationConfig.ApplicationConfiguration;
import com.yourstechnology.form.features.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {
    private final ApplicationConfiguration config;

    private PrivateKey AccessTokenPrivateKey;
    private PublicKey AccessTokenPublicKey;
    // private PrivateKey RefreshTokenPrivateKey;
    // private PublicKey RefreshTokenPublicKey;

    @PostConstruct
    public void loadKeys() {
        try {
            AccessTokenPrivateKey = loadPrivateKey(config.getToken().getAccess().getPrivateKeyPath());
            AccessTokenPublicKey = loadPublicKey(config.getToken().getAccess().getPublicKeyPath());
            // RefreshTokenPrivateKey = loadPrivateKey(config.getToken().getRefresh().getPrivateKeyPath());
            // RefreshTokenPublicKey = loadPublicKey(config.getToken().getRefresh().getPublicKeyPath());
        } catch (Exception e) {
            log.error("Error loading keys", e);
        }
    }

    private PrivateKey loadPrivateKey(String path)
            throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        byte[] keyBytes = Files.readAllBytes(Paths.get(path));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    private PublicKey loadPublicKey(String path) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        byte[] keyBytes = Files.readAllBytes(Paths.get(path));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateAccessToken(
            UserDetails userDetails) {

        return Jwts
                .builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(
                        new Date(System.currentTimeMillis() + config.getToken().getAccess().getExpiration().toMillis()))
                .signWith(AccessTokenPrivateKey)
                .compact();
    }

    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getEmail())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(AccessTokenPublicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
