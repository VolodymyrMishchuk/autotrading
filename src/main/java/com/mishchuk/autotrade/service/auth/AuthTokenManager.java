package com.mishchuk.autotrade.service.auth;

import com.mishchuk.autotrade.repository.AuthTokenRepository;
import com.mishchuk.autotrade.repository.entity.AuthTokenEntity;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthTokenManager {

    private final AuthTokenRepository authTokenRepository;
    private static final int REFRESH_TOKEN_TTL_DAYS = 30;

    public AuthTokenEntity create(UserEntity user, String refreshToken) {
        Instant expiry = Instant.now().plus(REFRESH_TOKEN_TTL_DAYS, ChronoUnit.DAYS);
        AuthTokenEntity entity = AuthTokenEntity.builder()
                .user(user)
                .refreshToken(refreshToken)
                .expiryDate(expiry)
                .createdAt(Instant.now())
                .build();
        return authTokenRepository.save(entity);
    }

    public Optional<AuthTokenEntity> findByToken(String refreshToken) {
        return authTokenRepository.findByRefreshToken(refreshToken);
    }

    public void deleteByToken(String refreshToken) {
        authTokenRepository.deleteByRefreshToken(refreshToken);
    }
}