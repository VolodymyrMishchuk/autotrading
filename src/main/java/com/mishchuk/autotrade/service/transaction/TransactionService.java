package com.mishchuk.autotrade.service.transaction;

import com.mishchuk.autotrade.controller.dto.TransactionCreateDto;
import com.mishchuk.autotrade.controller.dto.TransactionDetailDto;

import java.util.UUID;

public interface TransactionService {
    TransactionDetailDto createTransaction(TransactionCreateDto request);
    TransactionDetailDto getTransaction(UUID id);
}