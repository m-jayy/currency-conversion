package com.mohsin.mycurrencyconverterapp.data.remote.dto

data class ExchangeRatesApiModel(
    val base: String,
    val rates: Map<String, Double>
)
