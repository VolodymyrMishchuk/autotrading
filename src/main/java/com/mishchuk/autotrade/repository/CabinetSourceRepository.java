package com.mishchuk.autotrade.repository;

import com.mishchuk.autotrade.repository.entity.CabinetSourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CabinetSourceRepository extends JpaRepository<CabinetSourceEntity, UUID> {

    List<CabinetSourceEntity> findByCabinetId(UUID cabinetId);

    List<CabinetSourceEntity> findBySourceId(UUID sourceId);

    Optional<CabinetSourceEntity> findByCabinetIdAndSourceId(UUID cabinetId, UUID sourceId);

    void deleteByCabinetIdAndSourceId(UUID cabinetId, UUID sourceId);
}