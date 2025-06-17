package com.wex.transaction.infrastructure.adapter.out.integration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wex.transaction.domain.model.ExchangeRateResponse;

import com.wex.transaction.domain.port.out.TreasuryRatesClientPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class TreasuryRatesApiClient implements TreasuryRatesClientPort {

    @Value("${treasury.api.base-url}")
    private String baseUrl;
    @Value("${treasury.api.fields}")
    private String fields;
    @Value("${treasury.api.filter}")
    private String filter;

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
            String formattedFilter = String.format(filter, countryCurrency, recordDate);
            String absoluteUrl = String.format("%s%s%s", baseUrl, fields, formattedFilter);
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
