package com.mishchuk.autotrade.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mishchuk.autotrade.enums.Direction;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransactionDetailDto {
    private UUID id;
    private String symbol;
    private BigDecimal amount;
    private Direction direction;
    private Instant openedAt;
    private Instant closedAt;
    private BigDecimal balanceAfter;
    private Boolean isProfitable;
    private UUID userId;
    private UUID accountId;
    private UUID cabinetId;
    private UUID sourceId;
    private Instant createdAt;
}