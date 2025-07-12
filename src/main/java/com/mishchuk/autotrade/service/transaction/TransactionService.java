package com.mishchuk.autotrade.service.transaction;

import com.mishchuk.autotrade.service.model.Transaction;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    void createTransaction(Transaction transaction);
    Transaction getTransactionById(UUID id);
    Transaction getTransactionByToken(UUID token);
    List<Transaction> getAllTransactions();
}