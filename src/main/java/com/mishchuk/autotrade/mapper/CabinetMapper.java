package com.mishchuk.autotrade.mapper;

import com.mishchuk.autotrade.controller.dto.CabinetCreateDto;
import com.mishchuk.autotrade.controller.dto.CabinetDetailDto;
import com.mishchuk.autotrade.controller.dto.CabinetUpdateDto;
import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.repository.entity.AccountEntity;
import com.mishchuk.autotrade.repository.entity.CabinetEntity;
import com.mishchuk.autotrade.repository.entity.SourceEntity;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import com.mishchuk.autotrade.service.model.Cabinet;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.UUID;

@Component
public class CabinetMapper {

    public static Cabinet toCabinet(CabinetEntity entity) {
        return Cabinet.builder()
                .id(entity.getId() != null ? entity.getId() : null)
                .name(entity.getName())
                .metaTradeToken(entity.getMetaTradeToken())
                .status(entity.getStatus())
                .userId(entity.getUser().getId())
                .accountId(entity.getAccount().getId())
                .sourceIds(entity.getSources().stream()
                        .map(SourceEntity::getId)
                        .toList())
                .build();
    }

    public static CabinetEntity toCabinetEntity(
            Cabinet cabinet,
            UserEntity user,
            AccountEntity account,
            List<SourceEntity> sources
    ) {
        return CabinetEntity.builder()
                .id(cabinet.getId() != null ? cabinet.getId() : null)
                .name(cabinet.getName())
                .metaTradeToken(cabinet.getMetaTradeToken())
                .status(cabinet.getStatus())
                .user(user)
                .account(account)
                .sources(sources)
                .build();
    }

    public static CabinetDetailDto toCabinetDetailDto(Cabinet cabinet) {
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

    public static Cabinet toCabinet(UUID userId, CabinetCreateDto dto) {
        return Cabinet.builder()
                .name(dto.getName())
                .metaTradeToken(dto.getMetaTradeToken())
                .status(Status.ACTIVE)
                .userId(userId)
                .accountId(dto.getAccountId())
                .sourceIds(dto.getSourceIds())
                .build();
    }

    public static Cabinet toCabinet(CabinetUpdateDto dto) {
        return Cabinet.builder()
                .name(dto.getName())
                .metaTradeToken(dto.getMetaTradeToken())
                .status(dto.getStatus())
                .sourceIds(dto.getSourceIds())
                .build();
    }

    public static Cabinet mergeCabinet(Cabinet original, Cabinet updates) {
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