package com.mishchuk.autotrade.service.transaction;

import com.mishchuk.autotrade.controller.dto.TransactionCreateDto;
import com.mishchuk.autotrade.controller.dto.TransactionDetailDto;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    TransactionDetailDto createTransaction(TransactionCreateDto dto);
    TransactionDetailDto getTransactionById(UUID id);
    TransactionDetailDto getTransactionByToken(String token);
    List<TransactionDetailDto> getTransactionsByUser(UUID userId);
    List<TransactionDetailDto> getTransactionsByCabinet(UUID cabinetId);
    List<TransactionDetailDto> getTransactionsBySource(UUID sourceId);
    List<TransactionDetailDto> getTransactionsByCabinetAndSource(UUID cabinetId, UUID sourceId);
    List<TransactionDetailDto> getAllTransactions();
}