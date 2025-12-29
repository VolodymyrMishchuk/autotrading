package com.mishchuk.autotrade.ingest.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mishchuk.autotrade.enums.Direction;
import com.mishchuk.autotrade.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)public class SignalMessage {
    private String sourceId;
    private Type type;
    private String symbol;
    private Direction direction;
    private BigDecimal amount;
    private Long chatId;
    private Integer messageId;
    private Long dateTs;
    private String rawText;
}
