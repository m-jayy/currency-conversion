package com.mohsin.mycurrencyconverterapp.testHelper

import com.mohsin.mycurrencyconverterapp.domain.model.CurrencyUiModel
import com.mohsin.mycurrencyconverterapp.domain.model.ExchangeRateUIModel
import com.mohsin.mycurrencyconverterapp.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.channels.BufferOverflow
import java.io.IOException

class TestRepository : CurrencyRepository {

    // Shared flows to emit currency and exchange rate data
    private val currenciesFlow = MutableSharedFlow<List<CurrencyUiModel>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val exchangeRatesFlow = MutableSharedFlow<List<ExchangeRateUIModel>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val currenciesList: Flow<List<CurrencyUiModel>> = currenciesFlow
    override val exchangeRatesList: Flow<List<ExchangeRateUIModel>> = exchangeRatesFlow

    // Method to send a list of currencies to the flow
    fun sendCurrenciesModel(list: List<CurrencyUiModel>) = currenciesFlow.tryEmit(list)

    // Method to send a list of exchange rates to the flow
    fun sendCurrencyRatesModel(list: List<ExchangeRateUIModel>) = exchangeRatesFlow.tryEmit(list)

    // Flag to track if the updateCurrenciesAndRates method has been called
    var hasTriedUpdateCurrenciesAndRates = false

    // Flag to simulate an error in the updateCurrenciesAndRates method
    private var shouldThrowsError = false

    // Method to set whether the updateCurrenciesAndRates method should throw an error
    fun setShouldThrowsError(value: Boolean) {
        shouldThrowsError = value
    }

    // Simulated method to update currencies and rates
    override suspend fun updateCurrenciesAndRates() {
        if (shouldThrowsError) {
            throw IOException("Network Error")
        }
        hasTriedUpdateCurrenciesAndRates = true
    }
}
