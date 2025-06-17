package com.wex.transaction.infrastructure.adapter.out.persistence;

import com.wex.transaction.domain.model.Transaction;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class TransactionEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "description")
    private String description;
    @Column(name = "transactionDate")
    private LocalDate transactionDate;
    @Column(name = "amount")
    private BigDecimal amount;

    public TransactionEntity() {}

    public TransactionEntity(Transaction transactionDomain) {
        this.id = transactionDomain.getId();
        this.description = transactionDomain.getDescription();
        this.transactionDate = transactionDomain.getTransactionDate();
        this.amount = transactionDomain.getAmount();
    }

    public Transaction toDomain() {
        return new Transaction(id, description, transactionDate, amount);
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

}
