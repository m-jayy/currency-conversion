package com.mohsin.mycurrencyconverterapp.data.dataSources

import com.mohsin.mycurrencyconverterapp.data.db.dao.CurrencyDao
import com.mohsin.mycurrencyconverterapp.data.db.dao.ExchangeRateDao
import com.mohsin.mycurrencyconverterapp.data.db.entity.CurrencyDbModel
import com.mohsin.mycurrencyconverterapp.data.db.entity.ExchangeRatesDbModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CurrencyLocalDataSource {
    fun getCurrenciesList(): Flow<List<CurrencyDbModel>>

    fun saveCurrencies(currencies: List<CurrencyDbModel>)

    fun getExchangeRatesList(base: String): Flow<List<ExchangeRatesDbModel>>

    fun saveExchangeRates(exchangeRateList: List<ExchangeRatesDbModel>)
}

class CurrencyLocalDataSourceImpl @Inject constructor(
    private val currencyDao: CurrencyDao,
    private val exchangeRatesDao: ExchangeRateDao
) : CurrencyLocalDataSource {
    override fun getCurrenciesList(): Flow<List<CurrencyDbModel>> {
        return currencyDao.getAll()
    }

    override fun saveCurrencies(currencies: List<CurrencyDbModel>) {
        return currencyDao.insertAll(currencies)
    }

    override fun getExchangeRatesList(base: String): Flow<List<ExchangeRatesDbModel>> {
        return exchangeRatesDao.getAllByBase(base)
    }

    override fun saveExchangeRates(exchangeRateList: List<ExchangeRatesDbModel>) {
        return exchangeRatesDao.insertAll(exchangeRateList)
    }

}