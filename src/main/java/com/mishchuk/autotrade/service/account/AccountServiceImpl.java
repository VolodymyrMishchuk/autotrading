package com.mishchuk.autotrade.service.account;

import com.mishchuk.autotrade.controller.dto.AccountCreateDto;
import com.mishchuk.autotrade.controller.dto.AccountDetailDto;
import com.mishchuk.autotrade.controller.dto.AccountUpdateDto;
import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.mapper.AccountMapper;
import com.mishchuk.autotrade.repository.AccountRepository;
import com.mishchuk.autotrade.repository.UserRepository;
import com.mishchuk.autotrade.repository.entity.AccountEntity;
import com.mishchuk.autotrade.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    public AccountDetailDto create(AccountCreateDto dto, UUID requesterId, boolean requesterIsAdmin) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body is required");
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account name is required");
        }
        if (dto.getAccountType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account type is required");
        }

        // якщо користувач створює сам — змушуємо userId = requesterId
        UUID ownerId = requesterIsAdmin
                ? (dto.getUserId() == null
                ? throwBad("user_id is required for admin creation")
                : dto.getUserId())
                : requesterId;

        UserEntity user = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        AccountEntity entity = AccountEntity.builder()
                .name(dto.getName().trim())
                .user(user)
                .status(Status.ACTIVE)
                .balance(BigDecimal.ZERO)
                .accountType(dto.getAccountType())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        entity = accountRepository.save(entity);
        log.info("Account '{}' ({}) created for user {}", entity.getName(), entity.getId(), user.getEmail());
        return accountMapper.toAccountDetailDto(entity);
    }

    private UUID throwBad(String msg) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
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
    @Transactional(readOnly = true)
    public List<AccountDetailDto> getAllAccountDtos() {
        return accountRepository.findAll()
                .stream()
                .map(accountMapper::toAccountDetailDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDetailDto> getUserAccounts(UUID userId) {
        return accountRepository.findAllByUser_Id(userId)
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