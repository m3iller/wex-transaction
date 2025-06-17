package com.wex.transaction.infrastructure.adapter.out.persistence;

import com.wex.transaction.domain.model.Transaction;
import com.wex.transaction.domain.port.out.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepositoryImp implements TransactionRepository {

    private final JpaRepository<TransactionEntity, Long> transactionRepository;

    public TransactionRepositoryImp(JpaRepository<TransactionEntity, Long> transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity = new TransactionEntity(transaction);
        return transactionRepository.save(entity).toDomain();
    }

    @Override
    public Transaction getById(Long id) {
        TransactionEntity entity = transactionRepository.findById(id).orElse(null);
        if(entity == null) {
            throw new IllegalArgumentException("Transaction not found");
        }
        return entity.toDomain();
    }

    @Override
    public Page<Transaction> findAll(Pageable pageable) {
        Page<TransactionEntity> entityPaged = transactionRepository.findAll(pageable);
        return entityPaged.map(TransactionEntity::toDomain);
    }
}
