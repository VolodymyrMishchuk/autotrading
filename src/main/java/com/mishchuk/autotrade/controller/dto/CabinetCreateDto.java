package com.mishchuk.autotrade.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CabinetCreateDto {
    private String name;
    private String metaTradeToken;
    private UUID accountId;
    private UUID userId;
    private List<UUID> sourceIds;
}