package com.mishchuk.autotrade.controller.dto.cabinet;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mishchuk.autotrade.enums.Status;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CabinetUpdateDto {
    private String name;
    private String metaTradeToken;
    private Status status;
    private List<UUID> sourceIds;
}