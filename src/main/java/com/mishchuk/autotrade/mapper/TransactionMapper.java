package com.mishchuk.autotrade.mapper;

import com.mishchuk.autotrade.controller.dto.*;
import com.mishchuk.autotrade.enums.Direction;
import com.mishchuk.autotrade.repository.entity.AccountEntity;
import com.mishchuk.autotrade.repository.entity.SourceEntity;
import com.mishchuk.autotrade.repository.entity.TransactionEntity;
import com.mishchuk.autotrade.service.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransactionMapper {

    public Transaction toTransaction(TransactionEntity entity) {
        return Transaction.builder()
                .id(entity.getId() != null ? entity.getId().toString() : null)
                .amount(entity.getAmount())
                .direction(Direction.valueOf(entity.getDirection()))
                .createdAt(entity.getCreatedAt())
                .account(entity.getAccount().getId().toString())
                .source(entity.getSource().getId().toString())
                .build();
    }

    public Transaction toTransaction(TransactionCreateDto dto) {
        return Transaction.builder()
                .amount(dto.getAmount())
                .direction(dto.getDirection())
                .createdAt(dto.getCreatedAt())
                .account(dto.getAccountId())
                .source(dto.getSourceId().toString())
                .build();
    }

    public TransactionEntity toTransactionEntity(Transaction transaction) {
        return TransactionEntity.builder()
                .id(transaction.getId() != null ? UUID.fromString(transaction.getId()) : null)
                .amount(transaction.getAmount())
                .direction(String.valueOf(transaction.getDirection()))
                .createdAt(transaction.getCreatedAt())
                .account(
                        AccountEntity.builder()
                                .id(transaction.getAccount())
                                .build()
                )
                .source(
                        SourceEntity.builder()
                                .id(UUID.fromString(transaction.getSource()))
                                .build()
                )
                .build();
    }

    public TransactionDetailDto toTransactionDetailDto(Transaction transaction) {
        return TransactionDetailDto.builder()
                .id(transaction.getId() != null ? UUID.fromString(transaction.getId()) : null)
                .amount(transaction.getAmount())
                .direction(transaction.getDirection())
                .createdAt(transaction.getCreatedAt())
                .accountId(UUID.fromString(transaction.getAccount()))
                .sourceId(UUID.fromString(transaction.getSource()))
                .build();
    }
}
