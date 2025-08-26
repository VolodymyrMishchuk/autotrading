package com.mishchuk.autotrade.service.auth;

import com.mishchuk.autotrade.service.model.User;
import com.mishchuk.autotrade.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {

    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_FIRST_NAME = "first_name";
    private static final String CLAIM_LAST_NAME = "last_name";
    private static final String CLAIM_ROLE = "role";
    @Value("${spring.jwt.secret}")
    private String jwtSecret;
    @Value("${spring.jwt.ttl-millis}")
    private Long jwtTtlMillis;

    @Override
    public String createAccessToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtTtlMillis);

        Claims claims = Jwts.claims()
                .issuedAt(now)
                .expiration(expiration)
                .subject(String.valueOf(user.getId()))
                .add(CLAIM_ROLE, user.getRole().name())
                .add(CLAIM_EMAIL, user.getEmail())
                .add(CLAIM_FIRST_NAME, user.getFirstName())
                .add(CLAIM_LAST_NAME, user.getLastName())
                .build();

        return Jwts.builder()
                .claims(claims)
                .signWith(getSecretKey())
                .compact();
    }

    @Override
    public String createRefreshToken() {
        return java.util.UUID.randomUUID().toString();
    }

    @Override
    public boolean isValidAccessToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String getUserId(String token) {

        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    @Override
    public UserRole getUserRole(String token) {

        String typeValue = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(CLAIM_ROLE, String.class);

        return UserRole.valueOf(typeValue);
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

}
