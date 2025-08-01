package com.mishchuk.autotrade.repository;

import com.mishchuk.autotrade.repository.entity.SourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface SourceRepository extends JpaRepository<SourceEntity, UUID> {

    List<SourceEntity> findByAccountId(UUID accountId);

}