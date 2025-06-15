package com.mishchuk.autotrade.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountDetailDto {
    private UUID id;
    private double balance;
    private String currency;
    private int number;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID personId;
    private UUID sourceId;
    private Status status;
}
