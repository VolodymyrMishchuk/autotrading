package com.mishchuk.autotrade.service.account;

import com.mishchuk.autotrade.mapper.AccountMapper;
import com.mishchuk.autotrade.repository.AccountRepository;
import com.mishchuk.autotrade.service.model.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public void createAccount(Account account) {

        log.info("Creating new account");

        UUID token = UUID.randomUUID();

    }

    Account updateAccount(String id, Account account);
    Account getAccountById(String id);
    Account getAccountByToken(String token);
    List<Account> getAllAccounts();
    void deleteAccount(String id);
}