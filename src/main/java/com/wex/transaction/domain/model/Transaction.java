package com.wex.transaction.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Currency;

public class Transaction {

    private Long id;
    private String description;
    private LocalDate transactionDate;
    private BigDecimal amount;
    private Currency currency;
    private Currency convertedCurrency;
    private BigDecimal convertedAmount;

    public Transaction(Long id, String description, LocalDate transactionDate, BigDecimal amount, Currency currency) {
        validateTransaction(description, transactionDate, amount);
        this.id = id;
        this.description = description;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.currency = currency;
    }

    public void validateTransaction(String description, LocalDate transactionDate, BigDecimal amount){
        if (description == null || description.length() > 50)
            throw new IllegalArgumentException("Description must be <= 50 characters");
        if (transactionDate == null || transactionDate.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Invalid transaction date");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be positive");
    }

    public void calcWithRate(BigDecimal rateCurrency,
                                             Currency currencyToConvert) {
        this.convertedAmount = this.getAmount().multiply(rateCurrency).setScale(2, RoundingMode.HALF_UP);
        this.convertedCurrency = currencyToConvert;
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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Currency getConvertedCurrency() {
        return convertedCurrency;
    }

    public void setConvertedCurrency(Currency convertedCurrency) {
        this.convertedCurrency = convertedCurrency;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }
}
