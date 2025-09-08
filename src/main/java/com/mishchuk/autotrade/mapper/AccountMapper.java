package com.mishchuk.autotrade.mapper;

import com.mishchuk.autotrade.controller.dto.account.AccountCreateDto;
import com.mishchuk.autotrade.controller.dto.account.AccountDetailDto;
import com.mishchuk.autotrade.controller.dto.account.AccountUpdateDto;
import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.repository.entity.AccountEntity;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import com.mishchuk.autotrade.service.model.Account;
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

    public AccountEntity toAccountEntity(AccountCreateDto dto, UserEntity user) {
        return AccountEntity.builder()
                .id(UUID.randomUUID())
                .name(dto.getName())
                .status(Status.PENDING)
                .accountType(dto.getAccountType())
                .user(user)
                .createdAt(Instant.now())
                .build();
    }

    public Account toAccount(AccountEntity entity) {
        return Account.builder()
                .id(entity.getId())
                .name(entity.getName())
                .status(entity.getStatus())
                .balance(entity.getBalance())
                .userId(entity.getUser().getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .accountType(entity.getAccountType())
                .build();
    }

    public AccountEntity toAccountEntity(AccountUpdateDto dto, AccountEntity entity) {
        if (dto.getName() != null) entity.setName(dto.getName());
        entity.setUpdatedAt(Instant.now());
        return entity;
    }

    public AccountEntity toAccountEntity(Status status, AccountEntity entity) {
        entity.setStatus(status);
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
                .accountType(entity.getAccountType())
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