package com.wex.transaction.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ExchangeRateResponse(@JsonProperty("data") List<ExchangeRateData> data) {
}


