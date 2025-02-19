package com.mohsin.mycurrencyconverterapp.domain.repository

import com.mohsin.mycurrencyconverterapp.domain.model.ExchangeRateUIModel
import com.mohsin.mycurrencyconverterapp.domain.model.CurrencyUiModel
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {

    val currenciesList : Flow<List<CurrencyUiModel>>
    val exchangeRatesList : Flow<List<ExchangeRateUIModel>>
    suspend fun updateCurrenciesAndRates()
}