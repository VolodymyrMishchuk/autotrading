package com.mishchuk.autotrade.repository;

import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.repository.entity.TdSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TdSessionRepository extends JpaRepository<TdSessionEntity, UUID> {

    List<TdSessionEntity> findByStatus(Status status);

    Optional<TdSessionEntity> findByName(String name);

    Optional<TdSessionEntity> findByDbDir(String dbDir);
}
