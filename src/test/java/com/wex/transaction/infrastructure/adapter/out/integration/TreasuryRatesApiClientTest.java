package com.wex.transaction.infrastructure.adapter.out.integration;

import com.wex.transaction.domain.model.ExchangeRateResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = TreasuryRatesApiClient.class)
@TestPropertySource(properties = {
    "treasury.api.base-url=https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange",
    "treasury.api.fields=?fields=country,country_currency_desc,currency,exchange_rate,record_date",
    "treasury.api.filter=&filter=country_currency_desc:in:(%s),record_date:gte:%s"
})
public class TreasuryRatesApiClientTest {
    @Autowired
    private TreasuryRatesApiClient client;

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
