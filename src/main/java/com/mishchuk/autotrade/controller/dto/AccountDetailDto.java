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
public class AccountDetailDto {
    private final UUID id;
    private final String number;
    private final Status status;
    private final BigDecimal balance;
    private final String currency;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final UUID userId;
    private final UUID sourceId;
}