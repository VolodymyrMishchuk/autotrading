package com.mishchuk.autotrade.ingest.parser;

import com.mishchuk.autotrade.ingest.model.SignalMessage;
import com.mishchuk.autotrade.enums.Direction;
import com.mishchuk.autotrade.enums.Type;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class TelegramSignalParser {

    private static final Pattern OPEN_PATTERN = Pattern.compile("(BUY|SELL)\\s+([A-Z/]{3,15})\\s*(\\d+(?:\\.\\d+)?)?");
    private static final Pattern CLOSE_PATTERN = Pattern.compile("CLOSE\\s+([A-Z/]{3,15})");

    public SignalMessage parse(String rawText) {
        try {
            Matcher open = OPEN_PATTERN.matcher(rawText.toUpperCase());
            if (open.find()) {
                return SignalMessage.builder()
                        .type(Type.OPEN)
                        .direction(Direction.valueOf(open.group(1)))
                        .symbol(open.group(2))
                        .amount(open.group(3) != null ? new BigDecimal(open.group(3)) : BigDecimal.ONE)
                        .rawText(rawText)
                        .build();
            }

            Matcher close = CLOSE_PATTERN.matcher(rawText.toUpperCase());
            if (close.find()) {
                return SignalMessage.builder()
                        .type(Type.CLOSE)
                        .symbol(close.group(1))
                        .rawText(rawText)
                        .build();
            }

        } catch (Exception e) {
            log.error("❌ Error parsing raw message '{}': {}", rawText, e.getMessage());
        }
        return null;
    }
}
