package com.mishchuk.autotrade.service;

import com.mishchuk.autotrade.controller.dto.TransactionCreateDto;
import com.mishchuk.autotrade.controller.dto.TransactionDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    @Override
    public TransactionDetailDto createTransaction(TransactionCreateDto request) {
        // TODO: handle balance update, persist transaction
        return new TransactionDetailDto();
    }

    @Override
    public TransactionDetailDto getTransaction(UUID id) {
        // TODO: fetch from DB
        return new TransactionDetailDto();
    }
}