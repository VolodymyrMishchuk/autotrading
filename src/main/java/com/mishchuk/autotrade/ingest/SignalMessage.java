package com.mishchuk.autotrade.ingest;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mishchuk.autotrade.enums.Direction;
import com.mishchuk.autotrade.enums.Type;
import lombok.Data;
import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignalMessage {
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
