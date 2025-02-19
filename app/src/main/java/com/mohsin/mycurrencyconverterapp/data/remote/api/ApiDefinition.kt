package com.mohsin.mycurrencyconverterapp.data.remote.api

import com.mohsin.mycurrencyconverterapp.core.AppConstants
import com.mohsin.mycurrencyconverterapp.data.remote.dto.ExchangeRatesApiModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiDefinition {
    @GET("currencies.json")
    suspend fun getCurrenciesList(): Map<String, String>

    @GET("latest.json")
    suspend fun getExchangeRatesList(@Query("app_id") appId: String = AppConstants.APP_ID): ExchangeRatesApiModel

}