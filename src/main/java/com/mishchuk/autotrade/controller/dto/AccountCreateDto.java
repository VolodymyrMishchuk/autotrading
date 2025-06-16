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
public class AccountCreateDto {
    private final UUID id;
    private final Long number;
    private final Status status;
    private final BigDecimal balance;
    private final String currency;
    private final String tokenMetaTradeAPI;
    private final Instant createdAt;
    private final UUID personId;
}
