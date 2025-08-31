package com.mishchuk.autotrade.service.audit;

import com.mishchuk.autotrade.repository.UserActionAuditRepository;
import com.mishchuk.autotrade.repository.entity.UserActionAuditEntity;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final UserActionAuditRepository repo;
    public void log(UserEntity u, String action, String channel, String target,
                    String ip, String ua, boolean success) {
        repo.save(UserActionAuditEntity.builder()
                .user(u).action(action).channel(channel).target(target)
                .ip(ip).userAgent(ua).success(success).createdAt(Instant.now()).build());
    }
}