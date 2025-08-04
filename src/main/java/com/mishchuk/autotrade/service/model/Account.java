package com.mishchuk.autotrade.service.model;

import com.mishchuk.autotrade.enums.Status;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    private UUID id;
    private String name;
    private String tokenMetaTradeAPI;
    private Status status;
    private BigDecimal balance;

    private UUID userId;
    private List<UUID> cabinetIds;
    private List<UUID> transactionIds;

    private Instant createdAt;
    private Instant updatedAt;
}