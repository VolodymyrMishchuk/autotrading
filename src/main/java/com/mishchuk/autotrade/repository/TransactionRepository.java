package com.mishchuk.autotrade.repository;

import com.mishchuk.autotrade.repository.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {

    List<TransactionEntity> findAllByUser_Id(UUID userId);
    List<TransactionEntity> findAllByCabinet_Id(UUID cabinetId);
    List<TransactionEntity> findAllBySource_Id(UUID sourceId);
    List<TransactionEntity> findAllByUser_IdAndCabinet_Id(UUID userId, UUID cabinetId);
    List<TransactionEntity> findAllByUser_IdAndSource_Id(UUID userId, UUID sourceId);
    List<TransactionEntity> findAllByCabinet_IdAndSource_Id(UUID cabinetId, UUID sourceId);
    List<TransactionEntity> findAllByUser_IdAndCabinet_IdAndSource_Id(UUID userId, UUID cabinetId, UUID sourceId);

}