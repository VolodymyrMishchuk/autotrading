package com.mishchuk.autotrade.service.account;

import com.mishchuk.autotrade.controller.dto.AccountDetailDto;
import com.mishchuk.autotrade.controller.dto.AccountUpdateDto;
import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.exception.EntityNotFoundException;
import com.mishchuk.autotrade.mapper.AccountMapper;
import com.mishchuk.autotrade.repository.AccountRepository;
import com.mishchuk.autotrade.repository.UserRepository;
import com.mishchuk.autotrade.repository.entity.AccountEntity;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public AccountDetailDto createAccountForNewUser(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        AccountEntity account = AccountEntity.builder()
                .id(UUID.randomUUID())
                .name("Account for " + user.getEmail())
                .user(user)
                .status(Status.PENDING)
                .balance(BigDecimal.ZERO)
                .createdAt(Instant.now())
                .build();

        accountRepository.save(account);
        log.info("Account created for user: {}", user.getEmail());
        return accountMapper.toAccountDetailDto(account);
    }

    @Override
    @Transactional
    public AccountDetailDto updateAccount(UUID id, AccountUpdateDto dto) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        accountMapper.toAccountEntity(dto, account);

        accountRepository.save(account);
        return accountMapper.toAccountDetailDto(account);
    }

    @Override
    @Transactional
    public AccountDetailDto updateAccountStatus(UUID id, Status status) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        account.setStatus(status);
        account.setUpdatedAt(Instant.now());

        accountRepository.save(account);
        log.info("Account {} status changed to {}", id, status);

        return accountMapper.toAccountDetailDto(account);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDetailDto getAccountById(UUID id) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        return accountMapper.toAccountDetailDto(account);
    }

    @Override
    public AccountDetailDto getAccountByToken(String token) {
        var entityOpt = accountRepository.findAll()
                .stream()
                .filter(a -> token.equals(a.getTokenMetaTradeAPI()))
                .findFirst();

        if (entityOpt.isEmpty()) {
            throw new EntityNotFoundException("Account not found for token: " + token);
        }

        return accountMapper.toAccountDetailDto(entityOpt.get());
    }

    @Override
    public List<AccountDetailDto> getAllAccountDtos() {
        return accountRepository.findAll()
                .stream()
                .map(accountMapper::toAccountDetailDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteAccount(UUID id) {
        accountRepository.deleteById(id);
        log.info("Account deleted: {}", id);
    }
}