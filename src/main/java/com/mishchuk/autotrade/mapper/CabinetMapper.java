package com.mishchuk.autotrade.mapper;

import com.mishchuk.autotrade.controller.dto.cabinet.CabinetCreateDto;
import com.mishchuk.autotrade.controller.dto.cabinet.CabinetDetailDto;
import com.mishchuk.autotrade.controller.dto.cabinet.CabinetUpdateDto;
import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.repository.entity.*;
import com.mishchuk.autotrade.service.model.Cabinet;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CabinetMapper {

    public Cabinet toCabinet(CabinetEntity entity) {
        return Cabinet.builder()
                .id(entity.getId())
                .name(entity.getName())
                .metaTradeToken(entity.getMetaTradeToken())
                .status(entity.getStatus())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .accountId(entity.getAccount() != null ? entity.getAccount().getId() : null)
                .sourceIds(
                        entity.getSources() != null
                                ? entity.getSources().stream()
                                .map(cabinetSource -> cabinetSource.getSource().getId())
                                .collect(Collectors.toList())
                                : List.of()
                )
                .build();
    }

    public CabinetEntity toCabinetEntity(
            Cabinet cabinet,
            UserEntity user,
            AccountEntity account,
            List<SourceEntity> sources
    ) {
        List<CabinetSourceEntity> cabinetSources = sources.stream()
                .map(source -> CabinetSourceEntity.builder()
                        .cabinet(null)
                        .source(source)
                        .status(Status.ACTIVE)
                        .build())
                .collect(Collectors.toList());

        CabinetEntity cabinetEntity = CabinetEntity.builder()
                .id(cabinet.getId())
                .name(cabinet.getName())
                .metaTradeToken(cabinet.getMetaTradeToken())
                .status(cabinet.getStatus())
                .user(user)
                .account(account)
                .sources(cabinetSources)
                .build();

        cabinetSources.forEach(cs -> cs.setCabinet(cabinetEntity));
        return cabinetEntity;
    }

    public CabinetDetailDto toCabinetDetailDto(Cabinet cabinet) {
        return CabinetDetailDto.builder()
                .id(cabinet.getId())
                .name(cabinet.getName())
                .metaTradeToken(cabinet.getMetaTradeToken())
                .status(cabinet.getStatus())
                .userId(cabinet.getUserId())
                .accountId(cabinet.getAccountId())
                .sourceIds(cabinet.getSourceIds())
                .build();
    }

    public CabinetDetailDto toCabinetDetailDto(CabinetEntity entity) {
        return CabinetDetailDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .metaTradeToken(entity.getMetaTradeToken())
                .status(entity.getStatus())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .accountId(entity.getAccount() != null ? entity.getAccount().getId() : null)
                .sourceIds(
                        entity.getSources() != null
                                ? entity.getSources().stream()
                                .map(cs -> cs.getSource().getId())
                                .collect(Collectors.toList())
                                : List.of()
                )
                .build();
    }

    public Cabinet toCabinet(CabinetCreateDto dto) {
        return Cabinet.builder()
                .name(dto.getName())
                .metaTradeToken(dto.getMetaTradeToken())
                .status(Status.ACTIVE)
                .userId(dto.getUserId())
                .accountId(dto.getAccountId())
                .sourceIds(dto.getSourceIds())
                .build();
    }

    public Cabinet toCabinet(CabinetUpdateDto dto) {
        return Cabinet.builder()
                .name(dto.getName())
                .metaTradeToken(dto.getMetaTradeToken())
                .status(dto.getStatus())
                .sourceIds(dto.getSourceIds())
                .build();
    }

    public Cabinet mergeCabinet(Cabinet original, Cabinet updates) {
        return Cabinet.builder()
                .id(original.getId())
                .name(updates.getName() != null ? updates.getName() : original.getName())
                .metaTradeToken(updates.getMetaTradeToken() != null ? updates.getMetaTradeToken() : original.getMetaTradeToken())
                .status(updates.getStatus() != null ? updates.getStatus() : original.getStatus())
                .userId(original.getUserId())
                .accountId(original.getAccountId())
                .sourceIds(updates.getSourceIds() != null ? updates.getSourceIds() : original.getSourceIds())
                .build();
    }
}