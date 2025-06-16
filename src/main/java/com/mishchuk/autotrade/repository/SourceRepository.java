package com.mishchuk.autotrade.repository;

import com.mishchuk.autotrade.service.domain.Source;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SourceRepository extends JpaRepository<Source, UUID> {
}