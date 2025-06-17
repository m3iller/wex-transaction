package com.wex.transaction.infrastructure.adapter.in.controller;

import com.wex.transaction.domain.model.Transaction;
import com.wex.transaction.domain.port.in.service.TransactionServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Currency;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionServicePort transactionService;

    @PostMapping
    public Transaction create(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @GetMapping("/{id}")
    public Transaction get(@PathVariable Long id) {
        return transactionService.getTransaction(id);
    }

    @GetMapping
    public Page<Transaction> list(
            @PageableDefault(sort = "transactionDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return transactionService.listTransactions(pageable);
    }

    @GetMapping("/{id}/convert/{locale}")
    public Transaction convertTransaction(@PathVariable Long id,
                                          @PathVariable String locale) {
        return transactionService.convertTransactionCurrency(id, locale);
    }




}
