package com.mohsin.mycurrencyconverterapp.testHelper

import com.mohsin.mycurrencyconverterapp.data.dataSources.CurrencyLocalDataSource
import com.mohsin.mycurrencyconverterapp.data.db.entity.CurrencyDbModel
import com.mohsin.mycurrencyconverterapp.data.db.entity.ExchangeRatesDbModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class TestCurrencyLocalDataSource : CurrencyLocalDataSource {
    private val allCurrenciesFlow: MutableSharedFlow<List<CurrencyDbModel>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val allExchangeRatesFlow: MutableSharedFlow<List<ExchangeRatesDbModel>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)


    override fun getCurrenciesList(): Flow<List<CurrencyDbModel>> {
        return allCurrenciesFlow
    }

    override fun saveCurrencies(currencies: List<CurrencyDbModel>) {
        allCurrenciesFlow.tryEmit(currencies)
    }

    override fun getExchangeRatesList(base: String): Flow<List<ExchangeRatesDbModel>> {
        return allExchangeRatesFlow.map { list ->
            return@map list.filter { it.base == base }
        }
    }

    override fun saveExchangeRates(exchangeRateList: List<ExchangeRatesDbModel>) {
        allExchangeRatesFlow.tryEmit(exchangeRateList)
    }
}