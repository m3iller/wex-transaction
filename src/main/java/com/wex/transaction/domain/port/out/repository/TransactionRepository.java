package com.wex.transaction.domain.port.out.repository;

import com.wex.transaction.domain.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionRepository {

    Transaction save(Transaction transaction);
    Transaction getById(Long id);
    Page<Transaction> findAll(Pageable pageable);
}
