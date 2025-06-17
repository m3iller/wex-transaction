package com.wex.transaction.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(Long id) {
        super("Transaction not found with id: " + id);
    }
    public TransactionNotFoundException(String currency) {
        super("No exchange rate found for currency: " + currency);
    }
}
