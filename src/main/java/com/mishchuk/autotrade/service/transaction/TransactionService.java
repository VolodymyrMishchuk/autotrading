package com.mishchuk.autotrade.service.transaction;

import com.mishchuk.autotrade.service.model.Transaction;
import java.util.List;

public interface TransactionService {
    void createTransaction(Transaction transaction);
    Transaction getTransactionById(String id);
    Transaction getTransactionByToken(String token);
    List<Transaction> getAllTransactions();
}