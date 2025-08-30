package com.mishchuk.autotrade.repository;

import com.mishchuk.autotrade.enums.TokenPurpose;
import com.mishchuk.autotrade.repository.entity.UserActionTokenEntity;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserActionTokenRepository extends JpaRepository<UserActionTokenEntity, UUID> {

    Optional<UserActionTokenEntity> findByToken(String token);

    Optional<UserActionTokenEntity> findByTokenAndPurpose(String token, TokenPurpose purpose);

    void deleteAllByUserAndPurpose(UserEntity user, TokenPurpose purpose);
}