package com.mishchuk.autotrade.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;

import java.util.HashMap;
import java.util.Map;

@Converter(autoApply = false)
public class JsonMapConverter implements AttributeConverter<Map<String, Object>, Object> {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> TYPE =
            new TypeReference<>() {};

    @Override
    public Object convertToDatabaseColumn(Map<String, Object> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        try {
            String json = MAPPER.writeValueAsString(attribute);
            PGobject pgo = new PGobject();
            pgo.setType("jsonb");
            pgo.setValue(json);
            return pgo;
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not serialize Map to JSONB", e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(Object dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            String json;
            if (dbData instanceof PGobject pgo) {
                json = pgo.getValue();
            } else {
                json = dbData.toString();
            }
            if (json == null || json.isBlank()) {
                return new HashMap<>();
            }
            return MAPPER.readValue(json, TYPE);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not deserialize JSONB to Map", e);
        }
    }
}