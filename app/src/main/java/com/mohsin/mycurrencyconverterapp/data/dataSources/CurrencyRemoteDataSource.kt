package com.mohsin.mycurrencyconverterapp.data.dataSources

import com.mohsin.mycurrencyconverterapp.data.remote.api.ApiDefinition
import com.mohsin.mycurrencyconverterapp.data.remote.dto.ExchangeRatesApiModel
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

interface CurrencyRemoteDataSource {
    suspend fun getCurrenciesList() : Map<String, String>
    suspend fun getExchangeRatesList() : ExchangeRatesApiModel
}

@Singleton
class CurrencyRemoteDataSourceImpl @Inject constructor(retrofit : Retrofit) : CurrencyRemoteDataSource {

    private val apiDefinition = retrofit.create(ApiDefinition::class.java)

    override suspend fun getCurrenciesList(): Map<String, String> {
        return apiDefinition.getCurrenciesList()
    }

    override suspend fun getExchangeRatesList(): ExchangeRatesApiModel {
        return apiDefinition.getExchangeRatesList()
    }
}