package com.wex.transaction.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidLocaleException extends RuntimeException {
    public InvalidLocaleException(String locale) {
        super("Invalid locale: " + locale);
    }
}
