package com.wex.transaction.infrastructure.adapter.out.persistence;

import com.wex.transaction.domain.model.Transaction;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

@Entity
@Table(name = "transactions")
public class TransactionEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private LocalDate transactionDate;
    private BigDecimal amount;
    private String currency;

    public TransactionEntity() {}

    public TransactionEntity(Transaction transactionDomain) {
        this.id = transactionDomain.getId();
        this.description = transactionDomain.getDescription();
        this.transactionDate = transactionDomain.getTransactionDate();
        this.amount = transactionDomain.getAmount();
        this.currency = transactionDomain.getCurrency().getCurrencyCode();
    }

    public Transaction toDomain() {
        return new Transaction(id, description, transactionDate, amount, Currency.getInstance(currency));
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
