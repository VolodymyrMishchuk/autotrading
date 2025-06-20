package com.mishchuk.autotrade.service.account;

import com.mishchuk.autotrade.controller.dto.AccountCreateDto;
import com.mishchuk.autotrade.controller.dto.AccountDetailDto;
import com.mishchuk.autotrade.controller.dto.AccountUpdateDto;

import java.util.UUID;

public interface AccountService {
    AccountDetailDto createAccount(AccountCreateDto request);
    AccountDetailDto updateAccount(UUID id, AccountUpdateDto request);
    AccountDetailDto getAccount(UUID id);
    void deleteAccount(UUID id);
}