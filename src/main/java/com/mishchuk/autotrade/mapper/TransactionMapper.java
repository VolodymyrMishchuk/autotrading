package com.mishchuk.autotrade.mapper;

import com.mishchuk.autotrade.controller.dto.transaction.TransactionCreateDto;
import com.mishchuk.autotrade.controller.dto.transaction.TransactionDetailDto;
import com.mishchuk.autotrade.repository.entity.*;
import com.mishchuk.autotrade.service.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

    public Transaction toTransaction(TransactionEntity entity) {
        return Transaction.builder()
                .id(entity.getId())
                .symbol(entity.getSymbol())
                .amount(entity.getAmount())
                .direction(entity.getDirection())
                .openedAt(entity.getOpenedAt())
                .closedAt(entity.getClosedAt())
                .balanceAfter(entity.getBalanceAfter())
                .isProfitable(entity.getIsProfitable())
                .userId(entity.getUser().getId())
                .accountId(entity.getAccount().getId())
                .cabinetId(entity.getCabinet().getId())
                .sourceId(entity.getSource().getId())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public Transaction toTransaction(TransactionCreateDto dto) {
        return Transaction.builder()
                .symbol(dto.getSymbol())
                .amount(dto.getAmount())
                .direction(dto.getDirection())
                .openedAt(dto.getOpenedAt())
                .closedAt(dto.getClosedAt())
                .balanceAfter(dto.getBalanceAfter())
                .isProfitable(dto.getIsProfitable())
                .userId(dto.getUserId())
                .accountId(dto.getAccountId())
                .cabinetId(dto.getCabinetId())
                .sourceId(dto.getSourceId())
                // createdAt як правило встановлюєш у сервісі при створенні (Instant.now())
                .build();
    }

    public TransactionEntity toTransactionEntity(
            Transaction tx,
            UserEntity user,
            AccountEntity account,
            CabinetEntity cabinet,
            SourceEntity source
    ) {
        return TransactionEntity.builder()
                .id(tx.getId())
                .symbol(tx.getSymbol())
                .amount(tx.getAmount())
                .direction(tx.getDirection())
                .openedAt(tx.getOpenedAt())
                .closedAt(tx.getClosedAt())
                .balanceAfter(tx.getBalanceAfter())
                .isProfitable(tx.getIsProfitable())
                .user(user)
                .account(account)
                .cabinet(cabinet)
                .source(source)
                .createdAt(tx.getCreatedAt())
                .build();
    }

    public TransactionDetailDto toTransactionDetailDto(Transaction tx) {
        return TransactionDetailDto.builder()
                .id(tx.getId())
                .symbol(tx.getSymbol())
                .amount(tx.getAmount())
                .direction(tx.getDirection())
                .openedAt(tx.getOpenedAt())
                .closedAt(tx.getClosedAt())
                .balanceAfter(tx.getBalanceAfter())
                .isProfitable(tx.getIsProfitable())
                .userId(tx.getUserId())
                .accountId(tx.getAccountId())
                .cabinetId(tx.getCabinetId())
                .sourceId(tx.getSourceId())
                .createdAt(tx.getCreatedAt())
                .build();
    }

    public TransactionDetailDto toTransactionDetailDto(TransactionEntity entity) {
        return toTransactionDetailDto(toTransaction(entity));
    }
    /*
    логіка мапінгу з TransactionEntity до TransactionDetailDto вже реалізована через проміжний обʼєкт Transaction
    public TransactionDetailDto toTransactionDetailDto(TransactionEntity entity) {
        return TransactionDetailDto.builder()
                .id(entity.getId())
                .symbol(entity.getSymbol())
                .amount(entity.getAmount())
                .direction(entity.getDirection())
                .openedAt(entity.getOpenedAt())
                .closedAt(entity.getClosedAt())
                .balanceAfter(entity.getBalanceAfter())
                .isProfitable(entity.getIsProfitable())
                .userId(entity.getUser().getId())
                .accountId(entity.getAccount().getId())
                .cabinetId(entity.getCabinet().getId())
                .sourceId(entity.getSource().getId())
                .createdAt(entity.getCreatedAt())
                .build();
    }
    */


    public List<TransactionDetailDto> toDtoList(List<TransactionEntity> entities) {
        return entities.stream()
                .map(this::toTransactionDetailDto)
                .collect(Collectors.toList());
    }
}