package com.wex.transaction.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TransactionException extends RuntimeException {
    public TransactionException(String message) {
        super("Invalid Transaction: " + message);
    }
}
