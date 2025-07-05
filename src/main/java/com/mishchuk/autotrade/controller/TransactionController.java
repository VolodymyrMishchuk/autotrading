package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.*;
import com.mishchuk.autotrade.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDetailDto> createTransaction(@Valid @RequestBody TransactionCreateDto request) {
        return ResponseEntity.ok(transactionService.createTransaction(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDetailDto> getTransaction(@PathVariable UUID id) {
        return ResponseEntity.ok(transactionService.getTransaction(id));
    }
}