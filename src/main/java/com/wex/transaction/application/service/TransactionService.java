package com.wex.transaction.application.service;

import com.wex.transaction.domain.common.CountryCurrencyFormatter;
import com.wex.transaction.domain.model.ExchangeRateData;
import com.wex.transaction.domain.model.ExchangeRateResponse;
import com.wex.transaction.domain.model.Transaction;
import com.wex.transaction.domain.port.in.service.TransactionServicePort;
import com.wex.transaction.domain.port.out.repository.TransactionRepository;
import com.wex.transaction.domain.exception.TransactionNotFoundException;
import com.wex.transaction.domain.exception.InvalidLocaleException;
import com.wex.transaction.infrastructure.adapter.out.integration.TreasuryRatesApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;

@Service
public class TransactionService implements TransactionServicePort {

    @Autowired
    private TreasuryRatesApiClient treasuryRatesApiClient;
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction createTransaction(Transaction tx) {
        return transactionRepository.save(tx);
    }

    @Override
    public Transaction getTransaction(Long id) {
        return transactionRepository.getById(id);
    }

    @Override
    public Transaction convertTransactionCurrency(Long transactionId, String locale) {
        if(!CountryCurrencyFormatter.isValidLocale(locale)) {
            throw new InvalidLocaleException(locale);
        }

        Transaction transaction = getTransaction(transactionId);
        String inputCurrency = CountryCurrencyFormatter.format(locale);
        if (transaction == null) {
            throw new TransactionNotFoundException(transactionId);
        }

        ExchangeRateData targetRate = fetchRates(inputCurrency,
            transaction.getTransactionDate().toString());

        BigDecimal targetRateValue = new BigDecimal(targetRate.exchangeRate());

        transaction.setExchangeRate(targetRateValue);
        transaction.calcWithRate(transaction.getAmount(), targetRateValue);
        return transaction;
    }

    @Override
    public Page<Transaction> listTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    public ExchangeRateData fetchRates(String currency, String recordDate) {
        LocalDate parsedRecordDate = LocalDate.parse(recordDate);
        LocalDate date6Months = parsedRecordDate.minusMonths(6);

        ExchangeRateResponse response = treasuryRatesApiClient.getExchangeRates(currency, date6Months.toString());
        
        // First try to find exact match for the requested date
        ExchangeRateData exactMatch = response.data().stream()
            .filter(rate -> rate.countryCurrencyDesc().equals(currency))
            .filter(rate -> rate.recordDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(parsedRecordDate))
            .findFirst()
            .orElse(null);

        if (exactMatch != null) {
            return exactMatch;
        }

        // If no exact match, find the closest date
        return response.data().stream()
            .filter(rate -> rate.countryCurrencyDesc().equals(currency))
            .sorted((r1, r2) -> {
                LocalDate date1 = r1.recordDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate date2 = r2.recordDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                long diff1 = ChronoUnit.DAYS.between(date1, parsedRecordDate);
                long diff2 = ChronoUnit.DAYS.between(date2, parsedRecordDate);

                return Long.compare(diff1, diff2);
            })
            .findFirst()
            .orElseThrow(() -> new TransactionNotFoundException(currency));
    }
}
