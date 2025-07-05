package com.mishchuk.autotrade.service.auth;

import com.mishchuk.autotrade.service.model.User;
import com.mishchuk.autotrade.service.model.UserRole;
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
public class AuthServiceImpl implements AuthService{

    private static final String CLAIM_ROLE = "role";
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.ttl-millis}")
    private Long jwtTtlMillis;

    @Override
    public String createToken(User user) {

        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtTtlMillis);

        Claims claims = Jwts.claims()
                .issuedAt(now)
                .expiration(expiration)
                .subject(String.valueOf(user.getId()))
                .add(CLAIM_ROLE, user.getRole().toString())
                .build();

        return Jwts.builder()
                .claims(claims)
                .signWith(getSecretKey())
                .compact();
    }

    @Override
    public boolean isValidToken(String token) {

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
