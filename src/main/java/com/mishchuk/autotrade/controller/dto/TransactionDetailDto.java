package com.mishchuk.autotrade.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransactionDetailDto {
    private final UUID id;
    private final BigDecimal amount;
    private final Direction direction;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final UUID accountId;
    private final UUID sourceId;
}
