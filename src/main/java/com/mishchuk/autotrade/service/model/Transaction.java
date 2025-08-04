package com.mishchuk.autotrade.service.model;

import com.mishchuk.autotrade.enums.Direction;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
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