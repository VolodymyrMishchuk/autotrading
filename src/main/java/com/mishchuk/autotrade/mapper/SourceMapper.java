package com.mishchuk.autotrade.mapper;

import com.mishchuk.autotrade.controller.dto.*;
import com.mishchuk.autotrade.repository.entity.SourceEntity;
import com.mishchuk.autotrade.service.model.Source;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class SourceMapper {

    public Source toSource(SourceEntity entity) {
        return Source.builder()
                .id(entity.getId() != null ? entity.getId().toString() : null)
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
                .id(source.getId() != null ? UUID.fromString(source.getId()) : null)
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
                .id(source.getId() != null ? UUID.fromString(source.getId()) : null)
                .name(source.getName())
                .platform(source.getPlatform())
                .token(source.getToken())
                .status(source.getStatus())
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .build();
    }
}
