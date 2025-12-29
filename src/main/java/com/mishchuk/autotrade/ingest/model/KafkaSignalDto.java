package com.mishchuk.autotrade.ingest.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.Instant;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KafkaSignalDto(
        Long chatId,
        String chatTitle,
        Integer messageId,
        Long senderId,
        String text,
        Instant date,
        Instant editDate
) {}
