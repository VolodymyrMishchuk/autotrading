package com.mishchuk.autotrade.mapper;

import com.mishchuk.autotrade.controller.dto.CabinetSourceDto;
import com.mishchuk.autotrade.repository.entity.CabinetSourceEntity;
import org.springframework.stereotype.Component;

@Component
public class CabinetSourceMapper {

    public CabinetSourceDto toCabinetSourceDto(CabinetSourceEntity entity) {
        return CabinetSourceDto.builder()
                .id(entity.getId())
                .cabinetId(entity.getCabinet().getId())
                .sourceId(entity.getSource().getId())
                .status(entity.getStatus())
                .build();
    }
}