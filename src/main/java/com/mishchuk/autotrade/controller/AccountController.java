package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.account.AccountCreateDto;
import com.mishchuk.autotrade.controller.dto.account.AccountDetailDto;
import com.mishchuk.autotrade.controller.dto.account.AccountStatusUpdateDto;
import com.mishchuk.autotrade.controller.dto.account.AccountUpdateDto;
import com.mishchuk.autotrade.service.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // ---------- створення ----------
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AccountDetailDto> createAccount(
            @Valid @RequestBody AccountCreateDto body,
            Authentication auth
    ) {
        UUID requesterId = UUID.fromString(auth.getName());
        AccountDetailDto created = accountService.create(body, requesterId, false);
        return ResponseEntity
                .created(URI.create("/accounts/" + created.getId()))
                .body(created);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<AccountDetailDto> createAccountByAdmin(@Valid @RequestBody AccountCreateDto body,
                                                                 Authentication auth) {
        UUID requesterId = UUID.fromString(auth.getName());
        AccountDetailDto created = accountService.create(body, requesterId, true);
        return ResponseEntity
                .created(URI.create("/accounts/" + created.getId()))
                .body(created);
    }

    // ---------- читання ----------
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AccountDetailDto>> myAccounts(Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        return ResponseEntity.ok(accountService.getUserAccounts(userId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authHelper.isAccountOwner(#id, principal.username) or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<AccountDetailDto> getAccount(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<AccountDetailDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccountDtos());
    }

    // ---------- зміни ----------
    @PutMapping("/{id}")
    @PreAuthorize("@authHelper.isAccountOwner(#id, principal.username) or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<AccountDetailDto> updateAccount(@PathVariable UUID id,
                                                          @RequestBody AccountUpdateDto dto) {
        return ResponseEntity.ok(accountService.updateAccount(id, dto));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<AccountDetailDto> updateAccountStatus(@PathVariable UUID id,
                                                                @RequestBody AccountStatusUpdateDto dto) {
        return ResponseEntity.ok(accountService.updateAccountStatus(id, dto.getStatus()));
    }

    // ---------- видалення ----------
    @DeleteMapping("/{id}")
    @PreAuthorize("@authHelper.isAccountOwner(#id, principal.username) or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}