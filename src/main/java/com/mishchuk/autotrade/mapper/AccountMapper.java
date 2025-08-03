package com.mishchuk.autotrade.mapper;

import com.mishchuk.autotrade.controller.dto.*;
import com.mishchuk.autotrade.repository.entity.AccountEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    private final TransactionMapper transactionMapper;
    private final CabinetMapper cabinetMapper;

    public AccountEntity toAccountEntity(AccountCreateDto dto) {
        return AccountEntity.builder()
                .id(UUID.randomUUID())
                .name(dto.getName())
                .tokenMetaTradeAPI(dto.getTokenMetaTradeAPI())
                .createdAt(Instant.now())
                .build();
    }

    public AccountEntity toAccountEntity(AccountUpdateDto dto, AccountEntity entity) {
        entity.setName(dto.getName());
        entity.setStatus(dto.getStatus());
        entity.setTokenMetaTradeAPI(dto.getTokenMetaTradeAPI());
        entity.setUpdatedAt(Instant.now());
        return entity;
    }

    public AccountDetailDto toAccountDetailDto(AccountEntity entity) {
        return AccountDetailDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .status(entity.getStatus())
                .balance(entity.getBalance())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .userId(entity.getUser().getId())
                .cabinets(
                        entity.getCabinets() != null
                                ? entity.getCabinets().stream()
                                .map(cabinetMapper::toCabinetDetailDto)
                                .collect(Collectors.toList())
                                : List.of()
                )
                .transactions(
                        entity.getTransactions() != null
                                ? transactionMapper.toDtoList(entity.getTransactions())
                                : List.of()
                )
                .build();
    }
}