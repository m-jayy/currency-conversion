package com.mohsin.mycurrencyconverterapp.data.repository

import com.mohsin.mycurrencyconverterapp.core.AppConstants
import com.mohsin.mycurrencyconverterapp.core.utils.DispatcherProvider
import com.mohsin.mycurrencyconverterapp.data.dataSources.CurrencyLocalDataSource
import com.mohsin.mycurrencyconverterapp.data.dataSources.CurrencyRemoteDataSource
import com.mohsin.mycurrencyconverterapp.data.dataSources.ExchangeRatesDataStore
import com.mohsin.mycurrencyconverterapp.data.mapper.toDbModel
import com.mohsin.mycurrencyconverterapp.data.mapper.toModel
import com.mohsin.mycurrencyconverterapp.domain.model.ExchangeRateUIModel
import com.mohsin.mycurrencyconverterapp.domain.model.CurrencyUiModel
import com.mohsin.mycurrencyconverterapp.domain.repository.CurrencyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyRepositoryImpl @Inject constructor(
    private val currencyRemoteDataSource: CurrencyRemoteDataSource,
    private val currencyLocalDataSource: CurrencyLocalDataSource,
    private val exchangeRatesDataStore: ExchangeRatesDataStore,
    private val dispatcherProvider: DispatcherProvider
): CurrencyRepository {
    override val currenciesList: Flow<List<CurrencyUiModel>>
        get() = currencyLocalDataSource.getCurrenciesList().map { list ->
            list.map { it.toModel() }
        }
    override val exchangeRatesList: Flow<List<ExchangeRateUIModel>>
        get() = exchangeRatesDataStore.latestBase.flatMapLatest { base ->
            currencyLocalDataSource.getExchangeRatesList(base).map { list ->
                list.map { it.toModel() }
            }
        }

    private suspend fun isUpdateRequired(): Boolean {
        return exchangeRatesDataStore.lastUpdate.firstOrNull()?.let { lastUpdate ->
            System.currentTimeMillis() - lastUpdate >= AppConstants.UPDATE_INTERVAL
        } ?: true
    }

    override suspend fun updateCurrenciesAndRates(): Unit = withContext(dispatcherProvider.io) {
        if(isUpdateRequired()) {
            val deferredCurrencies = async { currencyRemoteDataSource.getCurrenciesList() }
            val deferredRates = async { currencyRemoteDataSource.getExchangeRatesList() }

            currencyLocalDataSource.saveCurrencies(deferredCurrencies.await().toDbModel())
            val exchangeRate = deferredRates.await()
            currencyLocalDataSource.saveExchangeRates(exchangeRate.toDbModel())
            exchangeRatesDataStore.setLastUpdate(System.currentTimeMillis())
            exchangeRatesDataStore.setLatestBase(exchangeRate.base)
        }
    }
}