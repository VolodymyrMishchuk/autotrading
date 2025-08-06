package com.mishchuk.autotrade.security;

import com.mishchuk.autotrade.controller.dto.CabinetDetailDto;
import com.mishchuk.autotrade.service.account.AccountService;
import com.mishchuk.autotrade.service.cabinet.CabinetService;
import com.mishchuk.autotrade.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("authHelper")
@RequiredArgsConstructor
public class AuthHelper {

    private final CabinetService cabinetService;
    private final AccountService accountService;
    private final UserService userService;

    public boolean isCabinetOwner(UUID cabinetId, String userId) {
        CabinetDetailDto cabinet = cabinetService.getCabinetById(cabinetId);
        return cabinet.getUserId().toString().equals(userId);
    }

    public boolean isAccountOwner(UUID accountId, String userId) {
        var account = accountService.getAccountById(accountId);
        return account.getUserId().toString().equals(userId);
    }

    public boolean isUserSelf(UUID userId, String principalId) {
        return userId.toString().equals(principalId);
    }
}