package com.wex.transaction.infrastructure.adapter.out.integration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wex.transaction.domain.model.ExchangeRateResponse;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class TreasuryRatesApiClient {
    private static final String BASE_URL = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange";
    private static final String FIELDS = "?fields=country,country_currency_desc,currency,exchange_rate,record_date";
    private static final String FILTER = "&filter=country_currency_desc:in:(%s),record_date:gte:%s";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public TreasuryRatesApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public ExchangeRateResponse getExchangeRates(String countryCurrency,
                                                 String recordDate) {
        try {
            String formattedFilter = String.format(FILTER, countryCurrency, recordDate);
            String absoluteUrl = String.format("%s%s%s", BASE_URL, FIELDS, formattedFilter);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(absoluteUrl))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), ExchangeRateResponse.class);
            } else {
                throw new RuntimeException("Failed to fetch exchange rates. Status code: " + response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching exchange rates", e);
        }
    }
}
