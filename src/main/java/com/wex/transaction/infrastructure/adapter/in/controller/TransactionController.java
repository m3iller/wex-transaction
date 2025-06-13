package com.wex.transaction.infrastructure.adapter.in.controller;

import com.wex.transaction.domain.model.Transaction;
import com.wex.transaction.domain.port.in.service.TransactionServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionServicePort transactionService;

    @PostMapping
    public Transaction create(@RequestBody Transaction tx) {
        return transactionService.createTransaction(tx);
    }

    @GetMapping("/{id}")
    public Transaction get(@PathVariable Long id) {
        return transactionService.getTransaction(id);
    }

    @GetMapping
    public Page<Transaction> list(Pageable pageable) {
        return transactionService.listTransactions(pageable);
    }
}
