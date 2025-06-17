package com.wex.transaction.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;

public record ExchangeRateData(
        @JsonProperty("country_currency_desc") String countryCurrencyDesc,
        @JsonProperty("exchange_rate") String exchangeRate,
        @JsonProperty("record_date") Date recordDate,
        @JsonProperty("country") String country,
        @JsonProperty("currency") String currency
) {
}
