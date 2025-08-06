package com.mishchuk.autotrade.mapper;

import com.mishchuk.autotrade.controller.dto.*;
import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.repository.entity.SourceEntity;
import com.mishchuk.autotrade.service.model.Source;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class SourceMapper {

    public Source toSource(SourceEntity entity) {
        return Source.builder()
                .id(entity.getId())
                .name(entity.getName())
                .platform(entity.getPlatform())
                .token(entity.getToken())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }


    public SourceEntity toSourceEntity(Source source) {
        return SourceEntity.builder()
                .id(source.getId())
                .name(source.getName())
                .platform(source.getPlatform())
                .token(source.getToken())
                .status(source.getStatus())
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .build();
    }

    public Source toSource(SourceCreateDto dto) {
        return Source.builder()
                .name(dto.getName())
                .platform(dto.getPlatform())
                .token(dto.getToken())
                .build();
    }

    public Source toSource(SourceUpdateDto dto) {
        return Source.builder()
                .name(dto.getName())
                .platform(dto.getPlatform())
                .token(dto.getToken())
                .status(dto.getStatus())
                .build();
    }

    public SourceDetailDto toSourceDetailDto(Source source) {
        return SourceDetailDto.builder()
                .id(source.getId())
                .name(source.getName())
                .platform(source.getPlatform())
                .token(source.getToken())
                .status(source.getStatus())
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .build();
    }

    public SourceDetailDto toSourceDetailDto(SourceEntity entity) {
        return SourceDetailDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .platform(entity.getPlatform())
                .token(entity.getToken())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public SourceEntity toSourceEntity(SourceCreateDto dto) {
        return SourceEntity.builder()
                .id(UUID.randomUUID())
                .name(dto.getName())
                .platform(dto.getPlatform())
                .token(dto.getToken())
                .status(Status.ACTIVE)
                .createdAt(Instant.now())
                .build();
    }

    public void updateSourceEntity(SourceUpdateDto dto, SourceEntity entity) {
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getPlatform() != null) entity.setPlatform(dto.getPlatform());
        if (dto.getStatus() != null) entity.setStatus(dto.getStatus());
        if (dto.getToken() != null) entity.setToken(dto.getToken());
        entity.setUpdatedAt(Instant.now());
    }
}
