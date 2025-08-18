package com.mishchuk.autotrade.ingest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mishchuk.autotrade.service.model.TelegramSettings;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class TelegramSettingsJsonbConverter implements AttributeConverter<TelegramSettings, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(TelegramSettings attribute) {
        if (attribute == null) return null;
        try {
            return MAPPER.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot serialize TelegramSettings to JSON", e);
        }
    }

    @Override
    public TelegramSettings convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) return new TelegramSettings();
        try {
            return MAPPER.readValue(dbData, TelegramSettings.class);
        } catch (Exception e) {
            return new TelegramSettings();
        }
    }
}
