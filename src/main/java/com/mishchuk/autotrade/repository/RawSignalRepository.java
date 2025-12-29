package com.mishchuk.autotrade.repository;

import com.mishchuk.autotrade.repository.entity.RawSignalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RawSignalRepository extends JpaRepository<RawSignalEntity, Long> {

    @Query("""
      SELECT coalesce(max(rs.version), 0)
      FROM RawSignalEntity rs
      WHERE rs.chatId = :chatId AND rs.messageId = :messageId
    """)
    int findMaxVersion(Long chatId, Long messageId);
}
