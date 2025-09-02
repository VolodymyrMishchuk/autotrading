package com.mishchuk.autotrade.repository.jpa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;

import java.util.Collections;
import java.util.Map;

@Converter(autoApply = false)
public class JsonbMapConverter implements AttributeConverter<Map<String, Object>, Object> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public Object convertToDatabaseColumn(Map<String, Object> attribute) {
        try {
            PGobject jsonb = new PGobject();
            jsonb.setType("jsonb");
            if (attribute == null || attribute.isEmpty()) {
                jsonb.setValue("{}");
            } else {
                jsonb.setValue(MAPPER.writeValueAsString(attribute));
            }
            return jsonb;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert Map to jsonb", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> convertToEntityAttribute(Object dbData) {
        if (dbData == null) {
            return Collections.emptyMap();
        }
        try {
            String json;
            if (dbData instanceof PGobject pg && ("json".equals(pg.getType()) || "jsonb".equals(pg.getType()))) {
                json = pg.getValue();
            } else {
                json = dbData.toString();
            }
            if (json == null || json.isBlank()) {
                return Collections.emptyMap();
            }
            return MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert jsonb to Map", e);
        }
    }
}