package com.mishchuk.autotrade.repository;

import com.mishchuk.autotrade.service.domain.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthTokenRepository extends JpaRepository<AuthToken, UUID> {
    Optional<AuthToken> findByRefreshToken(String refreshToken);
}