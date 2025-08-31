package com.mishchuk.autotrade.service.account;

import com.mishchuk.autotrade.controller.dto.account.AccountCreateDto;
import com.mishchuk.autotrade.controller.dto.account.AccountDetailDto;
import com.mishchuk.autotrade.controller.dto.account.AccountUpdateDto;
import com.mishchuk.autotrade.enums.Status;
import java.util.List;
import java.util.UUID;

public interface AccountService {

    AccountDetailDto create(AccountCreateDto dto, UUID requesterId, boolean requesterIsAdmin);

    AccountDetailDto updateAccount(UUID id, AccountUpdateDto dto);

    AccountDetailDto updateAccountStatus(UUID id, Status status);

    AccountDetailDto getAccountById(UUID id);

    List<AccountDetailDto> getAllAccountDtos();

    List<AccountDetailDto> getUserAccounts(UUID userId);

    void deleteAccount(UUID id);
}