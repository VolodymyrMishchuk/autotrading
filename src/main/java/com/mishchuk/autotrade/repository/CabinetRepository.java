package com.mishchuk.autotrade.repository;

import com.mishchuk.autotrade.repository.entity.CabinetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface CabinetRepository extends JpaRepository<CabinetEntity, UUID> {
    List<CabinetEntity> findByUserId(UUID userId);
    List<CabinetEntity> findByAccountId(UUID accountId);
}