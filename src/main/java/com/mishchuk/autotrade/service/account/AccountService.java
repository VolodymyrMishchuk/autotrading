package com.mishchuk.autotrade.service.account;

import com.mishchuk.autotrade.controller.dto.AccountDetailDto;
import com.mishchuk.autotrade.controller.dto.AccountUpdateDto;
import com.mishchuk.autotrade.enums.Status;
import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountDetailDto createAccountForNewUser(UUID userId);
    AccountDetailDto updateAccount(UUID id, AccountUpdateDto dto);
    AccountDetailDto updateAccountStatus(UUID id, Status status);
    AccountDetailDto getAccountById(UUID id);
    AccountDetailDto getAccountByToken(String token);
    List<AccountDetailDto> getAllAccountDtos();
    List<AccountDetailDto> getAllAccounts();
    void deleteAccount(UUID id);
}