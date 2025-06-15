package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.UUID;
import service.AccountService;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDetailDto> createAccount(@Valid @RequestBody AccountCreateDto request) {
        return ResponseEntity.ok(accountService.createAccount(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDetailDto> updateAccount(@PathVariable UUID id, @RequestBody AccountUpdateDto request) {
        return ResponseEntity.ok(accountService.updateAccount(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDetailDto> getAccount(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
