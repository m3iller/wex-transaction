package com.wex.transaction.application.service;

import com.wex.transaction.domain.common.CountryCurrencyFormatter;
import com.wex.transaction.domain.exception.TransactionException;
import com.wex.transaction.domain.model.ExchangeRateData;
import com.wex.transaction.domain.model.ExchangeRateResponse;
import com.wex.transaction.domain.model.Transaction;
import com.wex.transaction.domain.port.in.service.TransactionServicePort;
import com.wex.transaction.domain.port.out.repository.TransactionRepository;
import com.wex.transaction.domain.exception.TransactionNotFoundException;
import com.wex.transaction.domain.exception.InvalidLocaleException;
import com.wex.transaction.domain.port.out.TreasuryRatesClientPort;
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
    private TreasuryRatesClientPort exchangeRateClientPort;
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction createTransaction(Transaction transaction) {
        validateTransation(transaction);
        return transactionRepository.save(transaction);
    }

    private static void validateTransation(Transaction transaction) {
        if(transaction.getDescription() == null) {
            throw new TransactionException("Description is null");
        }
        if(transaction.getDescription().length() > 50 ) {
            throw new TransactionException("Description is too long , max is 50");
        }
        if(transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransactionException("Amount is null or negative");
        }
        if(transaction.getTransactionDate() == null) {
            throw new TransactionException("Transaction date is null");
        }
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

        // Use port to get all rates for the currency (for a range of dates)
        LocalDate parsedRecordDate = transaction.getTransactionDate();
        LocalDate date6Months = parsedRecordDate.minusMonths(6);
        ExchangeRateResponse response = exchangeRateClientPort.getExchangeRates(inputCurrency, date6Months.toString());

        // Find exact match for requested date
        ExchangeRateData exactMatch = response.data().stream()
            .filter(rate -> rate.countryCurrencyDesc().equals(inputCurrency))
            .filter(rate -> rate.recordDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(parsedRecordDate))
            .findFirst()
            .orElse(null);

        ExchangeRateData targetRate;
        if (exactMatch != null) {
            targetRate = exactMatch;
        } else {
            // If no exact match, find the closest date
            targetRate = response.data().stream()
                .filter(rate -> rate.countryCurrencyDesc().equals(inputCurrency))
                .sorted((r1, r2) -> {
                    LocalDate date1 = r1.recordDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate date2 = r2.recordDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    long diff1 = ChronoUnit.DAYS.between(date1, parsedRecordDate);
                    long diff2 = ChronoUnit.DAYS.between(date2, parsedRecordDate);
                    return Long.compare(diff1, diff2);
                })
                .findFirst()
                .orElseThrow(() -> new TransactionNotFoundException(inputCurrency));
        }

        BigDecimal targetRateValue = new BigDecimal(targetRate.exchangeRate());
        transaction.setExchangeRate(targetRateValue);
        transaction.calcWithRate(transaction.getAmount(), targetRateValue);
        return transaction;
    }

    @Override
    public Page<Transaction> listTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

}
