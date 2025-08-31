package com.mishchuk.autotrade.controller.dto.source;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SourceCreateDto {
    private final String name;
    private final String platform;
    private final String token;
}
