package com.mishchuk.autotrade.repository;

import com.mishchuk.autotrade.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByToken(UUID token);
    Optional<UserEntity> findByEmailIgnoreCase(String email);

}
