package com.mishchuk.autotrade.service.account;

import com.mishchuk.autotrade.service.model.Account;
import java.util.List;

public interface AccountService {
    void createAccount(Account account);
    Account updateAccount(String id, Account account);
    Account getAccountById(String id);
    Account getAccountByToken(String token);
    List<Account> getAllAccounts();
    void deleteAccount(String id);
}