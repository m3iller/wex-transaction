package com.wex.transaction.domain.port.out;

import com.wex.transaction.domain.model.ExchangeRateResponse;

public interface TreasuryRatesClientPort {
    ExchangeRateResponse getExchangeRates(String countryCurrency, String recordDate);
}
