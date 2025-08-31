package com.mishchuk.autotrade.controller.dto.cabinet;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mishchuk.autotrade.enums.Status;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CabinetSourceDto {
    private UUID id;
    private UUID cabinetId;
    private UUID sourceId;
    private Status status;
}