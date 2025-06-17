package com.wex.transaction.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Transaction {

    private Long id;
    private String description;
    private LocalDate transactionDate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;
    private BigDecimal exchangeRate;

    public Transaction(Long id, String description, LocalDate transactionDate, BigDecimal amount) {
        validateTransaction(description, transactionDate, amount);
        this.id = id;
        this.description = description;
        this.transactionDate = transactionDate;
        this.amount = amount;
    }

    public void validateTransaction(String description, LocalDate transactionDate, BigDecimal amount){
        if (description == null || description.length() > 50)
            throw new IllegalArgumentException("Description must be <= 50 characters");
        if (transactionDate == null || transactionDate.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Invalid transaction date");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be positive");
    }

    public void calcWithRate(BigDecimal valueInUsd,
                             BigDecimal targetCurrencyRate) {
        BigDecimal valueInTargetCurrency = valueInUsd.multiply(targetCurrencyRate);
        this.convertedAmount = valueInTargetCurrency.setScale(2, RoundingMode.HALF_UP);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
