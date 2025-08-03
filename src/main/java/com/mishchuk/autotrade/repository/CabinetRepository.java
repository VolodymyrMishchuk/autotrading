package com.mishchuk.autotrade.repository;

import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.repository.entity.CabinetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface CabinetRepository extends JpaRepository<CabinetEntity, UUID> {
    List<CabinetEntity> findByUser_Id(UUID userId);
    List<CabinetEntity> findByAccount_Id(UUID accountId);
    List<CabinetEntity> findByStatus(Status status);
    List<CabinetEntity> findByUser_Email(String email);
}