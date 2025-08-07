package com.mishchuk.autotrade.security;

import com.mishchuk.autotrade.controller.dto.AccountDetailDto;
import com.mishchuk.autotrade.controller.dto.CabinetDetailDto;
import com.mishchuk.autotrade.controller.dto.TransactionDetailDto;
import com.mishchuk.autotrade.service.account.AccountService;
import com.mishchuk.autotrade.service.cabinet.CabinetService;
import com.mishchuk.autotrade.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("authHelper")
@RequiredArgsConstructor
public class AuthHelper {

    private final TransactionService transactionService;
    private final CabinetService cabinetService;
    private final AccountService accountService;

    public boolean isUserSelf(UUID userId, String principalId) {
        return userId.toString().equals(principalId);
    }

    public boolean isCabinetOwner(UUID cabinetId, String userId) {
        CabinetDetailDto cabinet = cabinetService.getCabinetById(cabinetId);
        return cabinet.getUserId().toString().equals(userId);
    }

    public boolean isAccountOwner(UUID accountId, String userId) {
        AccountDetailDto account = accountService.getAccountById(accountId);
        return account.getUserId().toString().equals(userId);
    }

    public boolean isTransactionOwner(UUID transactionId, String userId) {
        TransactionDetailDto transaction = transactionService.getTransactionById(transactionId);
        return transaction.getUserId().toString().equals(userId);
    }
}