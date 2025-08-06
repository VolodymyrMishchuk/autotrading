package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.*;
import com.mishchuk.autotrade.service.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or @accountSecurity.isAccountOwner(#id, principal.id)")
    public ResponseEntity<AccountDetailDto> updateAccount(@PathVariable UUID id, @RequestBody AccountUpdateDto dto) {
        return ResponseEntity.ok(accountService.updateAccount(id, dto));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<AccountDetailDto> updateAccountStatus(
            @PathVariable UUID id,
            @RequestBody AccountStatusUpdateDto dto) {
        return ResponseEntity.ok(accountService.updateAccountStatus(id, dto.getStatus()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or @accountSecurity.isAccountOwner(#id, principal.id)")
    public ResponseEntity<AccountDetailDto> getAccount(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/by-token/{token}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<AccountDetailDto> getAccountByToken(@PathVariable String token) {
        return ResponseEntity.ok(accountService.getAccountByToken(token));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<AccountDetailDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccountDtos());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or @accountSecurity.isAccountOwner(#id, principal.id)")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
