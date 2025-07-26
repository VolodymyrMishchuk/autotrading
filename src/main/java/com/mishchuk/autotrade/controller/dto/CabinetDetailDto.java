package com.mishchuk.autotrade.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)public class CabinetDetailDto {
    private UUID id;
    private String name;
    private String metaTradeToken;
    private Status status;
    private UUID userId;
    private UUID accountId;
    private List<UUID> sourceIds;
}