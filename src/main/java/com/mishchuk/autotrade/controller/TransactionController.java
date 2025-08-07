package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.*;
import com.mishchuk.autotrade.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @PreAuthorize("#request.userId == principal.username or hasRole('SUPER_ADMIN')")
    public ResponseEntity<TransactionDetailDto> createTransaction(
            @Valid @RequestBody TransactionCreateDto request) {
        return ResponseEntity.ok(transactionService.createTransaction(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authHelper.isTransactionOwner(#id, principal.username) or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<TransactionDetailDto> getTransaction(@PathVariable UUID id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/by-user/{userId}")
    @PreAuthorize("#userId == principal.username or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<TransactionDetailDto>> getTransactionsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(transactionService.getTransactionsByUser(userId));
    }

    @GetMapping("/by-token/{token}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<TransactionDetailDto> getTransactionByToken(@PathVariable String token) {
        return ResponseEntity.ok(transactionService.getTransactionByToken(token));
    }

    @GetMapping("/by-cabinet/{cabinetId}")
    @PreAuthorize("@authHelper.isCabinetOwner(#cabinetId, principal.username) or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<TransactionDetailDto>> getTransactionsByCabinet(@PathVariable UUID cabinetId) {
        return ResponseEntity.ok(transactionService.getTransactionsByCabinet(cabinetId));
    }

    @GetMapping("/by-source/{sourceId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or principal.authorities.contains('ROLE_USER')")
    public ResponseEntity<List<TransactionDetailDto>> getTransactionsBySource(@PathVariable UUID sourceId) {
        // додаткову перевірку для user бажано в сервісі
        return ResponseEntity.ok(transactionService.getTransactionsBySource(sourceId));
    }

    @GetMapping("/by-cabinet/{cabinetId}/by-source/{sourceId}")
    @PreAuthorize("@authHelper.isCabinetOwner(#cabinetId, principal.username) or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<TransactionDetailDto>> getTransactionsByCabinetAndSource(
            @PathVariable UUID cabinetId,
            @PathVariable UUID sourceId) {
        return ResponseEntity.ok(transactionService.getTransactionsByCabinetAndSource(cabinetId, sourceId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<TransactionDetailDto>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
}
