package com.mohsin.mycurrencyconverterapp.testHelper

import com.mohsin.mycurrencyconverterapp.data.dataSources.CurrencyRemoteDataSource
import com.mohsin.mycurrencyconverterapp.data.remote.dto.ExchangeRatesApiModel

class TestCurrencyRemoteDataSource : CurrencyRemoteDataSource {
    override suspend fun getCurrenciesList(): Map<String, String> {
        return currenciesMap
    }

    override suspend fun getExchangeRatesList(): ExchangeRatesApiModel {
        return ExchangeRatesApiModel(
            base = "USD",
            rates = ratesMap
        )
    }

     val currenciesMap = mapOf(
        "JPY" to "Japanese Yen",
        "USD" to "United States Dollar",
        "EUR" to "Euro",
        "JPY" to "Japanese Yen",
        "GBP" to "British Pound Sterling",
        "AUD" to "Australian Dollar",
        "CAD" to "Canadian Dollar",
        "CHF" to "Swiss Franc",
        "CNY" to "Chinese Yuan",
        "INR" to "Indian Rupee",
        "NZD" to "New Zealand Dollar",
        "SGD" to "Singapore Dollar",
        "ZAR" to "South African Rand",
        "KRW" to "South Korean Won",
        "SEK" to "Swedish Krona",
    )

     private val ratesMap = mapOf(
        ("JPY" to 36.728375),
        "USD" to 1.0,
        "EUR" to 0.92,
        "JPY" to 144.95,
        "GBP" to 0.78,
        "AUD" to 1.49,
        "CAD" to 1.34,
        "CHF" to 0.88,
        "CNY" to 7.28,
        "INR" to 82.56,
        "NZD" to 1.61,
        "SGD" to 1.35,
        "ZAR" to 17.67,
        "KRW" to 1302.45,
        "SEK" to 10.51,
    )


}



