package com.wex.transaction.infrastructure.adapter.out.integration;

import com.wex.transaction.domain.model.ExchangeRateResponse;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TreasuryRatesApiClientTest {
    private TreasuryRatesApiClient client;

    @BeforeEach
    public void setUp() {
        client = new TreasuryRatesApiClient();
    }

    @Test
    public void testGetExchangeRates_Success() {
        LocalDate inputRecordDate = LocalDate.now().minusMonths(6);
        String recordDate = inputRecordDate.toString();

        ExchangeRateResponse response = client.getExchangeRates("Mexico-Peso", recordDate);
        assertNotNull(response);
        assertNotNull(response.data());
        assertTrue(response.data().stream()
                .anyMatch(rate -> rate.countryCurrencyDesc().equals("Mexico-Peso")),
            "Response should contain Mexico-Peso exchange rate");
    }

    @Test
    public void testGetExchangeRates_InvalidCurrency() {
        ExchangeRateResponse response = client.getExchangeRates("NonExistentCurrency", LocalDate.now().toString());
        assertTrue(response.data().isEmpty());
    }

}
