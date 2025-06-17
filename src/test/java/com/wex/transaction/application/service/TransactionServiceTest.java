package com.wex.transaction.application.service;

import com.wex.transaction.domain.model.Transaction;
import com.wex.transaction.domain.model.ExchangeRateData;
import com.wex.transaction.domain.model.ExchangeRateResponse;
import com.wex.transaction.domain.port.out.repository.TransactionRepository;
import com.wex.transaction.infrastructure.adapter.out.integration.TreasuryRatesApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TreasuryRatesApiClient treasuryRatesApiClient;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        testTransaction = new Transaction(
            1L,
            "Test Transaction",
            LocalDate.now().minusDays(1),
            new BigDecimal("100.00")
        );
    }

    private ExchangeRateResponse mockExchangeRateResponse(String currency, String rate) {
        Date now = new Date();
        ExchangeRateData data = new ExchangeRateData(
            currency + " Desc",
            rate,
            now,
            "US",
            currency
        );
        return new ExchangeRateResponse(Arrays.asList(data));
    }

    @Test
    void testCreateTransaction() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);
        Transaction created = transactionService.createTransaction(testTransaction);
        assertNotNull(created);
        assertEquals(testTransaction.getId(), created.getId());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testGetTransaction() {
        when(transactionRepository.getById(eq(1L))).thenReturn(testTransaction);
        Transaction found = transactionService.getTransaction(1L);
        assertNotNull(found);
        assertEquals(testTransaction.getId(), found.getId());
        verify(transactionRepository, times(1)).getById(1L);
    }

    @Test
    void testListTransactions() {
        List<Transaction> transactions = Arrays.asList(testTransaction);
        Page<Transaction> page = new PageImpl<>(transactions);
        when(transactionRepository.findAll(any(PageRequest.class))).thenReturn(page);
        Page<Transaction> result = transactionService.listTransactions(PageRequest.of(0, 10));
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(transactionRepository, times(1)).findAll(any(PageRequest.class));
    }



    @Test
    void testConvertTransactionCurrency_transactionNotFound() {
        when(transactionRepository.getById(eq(2L))).thenReturn(null);
        assertThrows(RuntimeException.class, () ->
            transactionService.convertTransactionCurrency(2L, "pt-BR")
        );
    }


}
