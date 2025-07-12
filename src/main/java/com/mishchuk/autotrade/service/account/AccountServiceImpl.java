package com.mishchuk.autotrade.service.account;

import com.mishchuk.autotrade.controller.dto.AccountCreateDto;
import com.mishchuk.autotrade.controller.dto.AccountDetailDto;
import com.mishchuk.autotrade.controller.dto.AccountUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    @Override
    public AccountDetailDto createAccount(AccountCreateDto request) {
        // TODO: save account with token_MetaTradeAPI
        return new AccountDetailDto();
    }

    @Override
    public AccountDetailDto updateAccount(UUID id, AccountUpdateDto request) {
        // TODO: update balance/status/token
        return new AccountDetailDto();
    }

    @Override
    public AccountDetailDto getAccount(UUID id) {
        // TODO: fetch by id
        return new AccountDetailDto();
    }

    @Override
    public void deleteAccount(UUID id) {
        // TODO: soft delete or hard delete
    }
}