package com.wex.transaction.domain.port.in.service;

import com.wex.transaction.domain.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionServicePort {
    Transaction createTransaction(Transaction tx);
    Transaction getTransaction(Long id);
    Transaction convertTransactionCurrency(Long transactionId, String currency);
    Page<Transaction> listTransactions(Pageable pageable);
}
