package com.mishchuk.autotrade.service.transaction;

import com.mishchuk.autotrade.controller.dto.TransactionCreateDto;
import com.mishchuk.autotrade.controller.dto.TransactionDetailDto;
import com.mishchuk.autotrade.exception.*;
import com.mishchuk.autotrade.mapper.TransactionMapper;
import com.mishchuk.autotrade.repository.*;
import com.mishchuk.autotrade.repository.entity.*;
import com.mishchuk.autotrade.service.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CabinetRepository cabinetRepository;
    private final SourceRepository sourceRepository;

    @Override
    public TransactionDetailDto createTransaction(TransactionCreateDto dto) {
        log.info("Creating new transaction");

        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User with id " + dto.getUserId() + " not found"));

        AccountEntity account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account with id " + dto.getAccountId() + " not found"));

        CabinetEntity cabinet = cabinetRepository.findById(dto.getCabinetId())
                .orElseThrow(() -> new CabinetNotFoundException("Cabinet with id " + dto.getCabinetId() + " not found"));

        SourceEntity source = sourceRepository.findById(dto.getSourceId())
                .orElseThrow(() -> new SourceNotFoundException("Source with id " + dto.getSourceId() + " not found"));

        Transaction tx = transactionMapper.toTransaction(dto);

        TransactionEntity entity = transactionMapper.toTransactionEntity(
                tx, user, account, cabinet, source
        );

        transactionRepository.save(entity);

        log.info("Transaction created successfully: {}", entity);

        return transactionMapper.toTransactionDetailDto(entity);
    }

    @Override
    public TransactionDetailDto getTransactionById(UUID id) {

        return transactionRepository.findById(id)
                .map(transactionMapper::toTransactionDetailDto)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with id " + id + " not found"));
    }

    @Override
    public List<TransactionDetailDto> getTransactionsByUser(UUID userId) {

        return transactionRepository.findAllByUser_Id(userId).stream()
                .map(transactionMapper::toTransactionDetailDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDetailDto> getTransactionsByCabinet(UUID cabinetId) {

        return transactionRepository.findAllByCabinet_Id(cabinetId).stream()
                .map(transactionMapper::toTransactionDetailDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDetailDto> getTransactionsBySource(UUID sourceId) {

        return transactionRepository.findAllBySource_Id(sourceId).stream()
                .map(transactionMapper::toTransactionDetailDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDetailDto> getTransactionsByCabinetAndSource(UUID cabinetId, UUID sourceId) {

        return transactionRepository.findAllByCabinet_IdAndSource_Id(cabinetId, sourceId).stream()
                .map(transactionMapper::toTransactionDetailDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDetailDto> getAllTransactions() {

        return transactionRepository.findAll().stream()
                .map(transactionMapper::toTransactionDetailDto)
                .collect(Collectors.toList());
    }
}