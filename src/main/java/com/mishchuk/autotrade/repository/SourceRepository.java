package com.mishchuk.autotrade.repository;

import com.mishchuk.autotrade.repository.entity.SourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SourceRepository extends JpaRepository<SourceEntity, UUID> {

    List<SourceEntity> findByAccountId(UUID accountId);

}