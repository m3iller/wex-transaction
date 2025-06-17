package com.wex.transaction.domain.common;

import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;

public final class CountryCurrencyFormatter {
    private CountryCurrencyFormatter() {}

    public static String format(String localeTag) {
        Locale loc = Locale.forLanguageTag(localeTag);
        String country = loc.getDisplayCountry(Locale.ENGLISH);
        if (country.isEmpty()) {
            throw new IllegalArgumentException("Invalid locale: " + localeTag);
        }
        Currency cur = Currency.getInstance(loc);
        String name = cur.getDisplayName(Locale.ENGLISH);

        return country + "-" + getLastWord(name);
    }

    public static boolean isValidLocale(String tag) {
        Locale loc = Locale.forLanguageTag(tag);
        String[] parts = tag.split("[-_]");
        if (parts.length < 2) return false;

        String lang = parts[0];
        String country = parts[1];
        boolean langOk = Arrays.asList(Locale.getISOLanguages()).contains(lang);
        boolean countryOk = Arrays.asList(Locale.getISOCountries()).contains(country.toUpperCase());
        return langOk && countryOk;
    }

    public static String getLastWord(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Input must not be null or blank");
        }
        String[] parts = input.trim().split("\\s+");
        return parts[parts.length - 1];
    }
}
