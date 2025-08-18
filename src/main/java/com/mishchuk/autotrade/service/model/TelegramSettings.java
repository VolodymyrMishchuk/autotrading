package com.mishchuk.autotrade.service.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelegramSettings {
    private String openRegex;
    private String closeRegex;
    private Map<String, String> symbolMap;
    public String openRegexOrDefault() {
        return (openRegex == null || openRegex.isBlank())
                ? "(BUY|SELL)\\s+([A-Z]{3,15})\\s*(\\d+(?:\\.\\d+)?)?"
                : openRegex;
    }
    public String closeRegexOrDefault() {
        return (closeRegex == null || closeRegex.isBlank())
                ? "CLOSE\\s+([A-Z]{3,15})"
                : closeRegex;
    }
}