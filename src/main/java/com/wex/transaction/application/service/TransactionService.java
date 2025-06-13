package com.wex.transaction.application.service;

import com.wex.transaction.domain.model.Transaction;
import com.wex.transaction.domain.port.in.service.TransactionServicePort;
import com.wex.transaction.domain.port.out.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransactionService implements TransactionServicePort {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction createTransaction(Transaction tx) {
        return transactionRepository.save(tx);
    }

    @Override
    public Transaction getTransaction(Long id) {
        return transactionRepository.getById(id);
    }

    @Override
    public Page<Transaction> listTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }
}
