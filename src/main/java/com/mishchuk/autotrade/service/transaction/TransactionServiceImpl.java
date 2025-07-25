package com.mishchuk.autotrade.service.transaction;

import com.mishchuk.autotrade.exception.TransactionNotFoundException;
import com.mishchuk.autotrade.mapper.TransactionMapper;
import com.mishchuk.autotrade.repository.TransactionRepository;
import com.mishchuk.autotrade.repository.entity.TransactionEntity;
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

    @Override
    public void createTransaction(Transaction transaction) {

        log.info("Creating new transaction");

        transaction.setAccount(transaction.getAccount());
        transaction.setAmount(transaction.getAmount());
        transaction.setDirection(transaction.getDirection());
        transaction.setDirection(transaction.getDirection());
        transaction.setCreatedAt(Instant.now());

        transactionRepository.save(transactionMapper.toTransactionEntity(transaction));

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