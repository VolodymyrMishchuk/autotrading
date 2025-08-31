package com.mishchuk.autotrade.repository;

import com.mishchuk.autotrade.repository.entity.UserActionAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserActionAuditRepository extends JpaRepository<UserActionAuditEntity, UUID> {}
