package com.mishchuk.autotrade.repository;

import com.mishchuk.autotrade.repository.entity.EmailVerificationTokenEntity;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationTokenRepository
        extends JpaRepository<EmailVerificationTokenEntity, UUID> {

    Optional<EmailVerificationTokenEntity> findByToken(String token);

    void deleteByExpiresAtBefore(Instant now);

    void deleteAllByUser(UserEntity user);
}