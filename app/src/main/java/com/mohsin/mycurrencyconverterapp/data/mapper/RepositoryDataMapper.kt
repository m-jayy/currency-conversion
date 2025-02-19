package com.mohsin.mycurrencyconverterapp.data.mapper

import com.mohsin.mycurrencyconverterapp.data.db.entity.CurrencyDbModel
import com.mohsin.mycurrencyconverterapp.data.db.entity.ExchangeRatesDbModel
import com.mohsin.mycurrencyconverterapp.data.remote.dto.ExchangeRatesApiModel
import com.mohsin.mycurrencyconverterapp.domain.model.CurrencyUiModel
import com.mohsin.mycurrencyconverterapp.domain.model.ExchangeRateUIModel

fun Map<String, String>.toDbModel(): List<CurrencyDbModel> {
    return map { CurrencyDbModel(currencyCode = it.key, currencyName = it.value) }

}

fun ExchangeRatesApiModel.toDbModel(): List<ExchangeRatesDbModel> {
    return rates.map { ExchangeRatesDbModel(base = base, currencyCode = it.key, rate = it.value) }
}

fun CurrencyDbModel.toModel(): CurrencyUiModel {
    return CurrencyUiModel(currencyCode = currencyCode, currencyName = currencyName)
}

fun ExchangeRatesDbModel.toModel(): ExchangeRateUIModel {
    return ExchangeRateUIModel(currencyCode = currencyCode, rate = rate, base = base)
}