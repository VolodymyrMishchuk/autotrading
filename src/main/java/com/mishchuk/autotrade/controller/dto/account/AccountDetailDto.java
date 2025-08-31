package com.mishchuk.autotrade.controller.dto.account;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mishchuk.autotrade.controller.dto.cabinet.CabinetDetailDto;
import com.mishchuk.autotrade.controller.dto.transaction.TransactionDetailDto;
import com.mishchuk.autotrade.enums.AccountType;
import com.mishchuk.autotrade.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountDetailDto {
    private final UUID id;
    private final String name;
    private final Status status;
    private final BigDecimal balance;
    private final AccountType accountType;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final UUID userId;
    private final List<CabinetDetailDto> cabinets;
    private final List<TransactionDetailDto> transactions;
}