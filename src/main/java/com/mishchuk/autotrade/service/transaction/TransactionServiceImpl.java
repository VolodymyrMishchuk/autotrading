package com.mishchuk.autotrade.service.transaction;

import com.mishchuk.autotrade.exception.*;
import com.mishchuk.autotrade.mapper.TransactionMapper;
import com.mishchuk.autotrade.repository.*;
import com.mishchuk.autotrade.repository.entity.*;
import com.mishchuk.autotrade.service.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public void createTransaction(Transaction transaction) {
        log.info("Creating new transaction");

        UserEntity user = userRepository.findById(transaction.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User with id " + transaction.getUserId() + " not found"));

        AccountEntity account = accountRepository.findById(transaction.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account with id " + transaction.getAccountId() + " not found"));

        CabinetEntity cabinet = cabinetRepository.findById(transaction.getCabinetId())
                .orElseThrow(() -> new CabinetNotFoundException("Cabinet with id " + transaction.getCabinetId() + " not found"));

        SourceEntity source = sourceRepository.findById(transaction.getSourceId())
                .orElseThrow(() -> new SourceNotFoundException("Source with id " + transaction.getSourceId() + " not found"));

        transaction.setCreatedAt(Instant.now());

        TransactionEntity entity = transactionMapper.toTransactionEntity(
                transaction, user, account, cabinet, source
        );

        transactionRepository.save(entity);

        log.info("Transaction created successfully: {}", transaction);
    }

    @Override
    public Transaction getTransactionById(String id) {

        Optional<TransactionEntity> optionalTransactionEntity =
                transactionRepository.findById(UUID.fromString(id));

        if (optionalTransactionEntity.isPresent()) {
            return  transactionMapper.toTransaction(optionalTransactionEntity.get());
        }

        throw new TransactionNotFoundException("Transaction with id " + id + " not found");
    }

    @Override
    public Transaction getTransactionByToken(String token) {

        Optional<TransactionEntity> optionalTransactionEntity =
                transactionRepository.findByToken(UUID.fromString(token));

        if (optionalTransactionEntity.isPresent()) {
            return  transactionMapper.toTransaction(optionalTransactionEntity.get());
        }

        throw new TransactionNotFoundException("Transaction with token " + token + " not found");
    }

    @Override
    public List<Transaction> getAllTransactions() {

        return transactionRepository
                .findAll()
                .stream()
                .map(transactionMapper::toTransaction)
                .toList();
    }
}