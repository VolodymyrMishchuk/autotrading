package com.mishchuk.autotrade.repository;

import com.mishchuk.autotrade.repository.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {

    Optional<TransactionEntity> findByToken(UUID token);

}