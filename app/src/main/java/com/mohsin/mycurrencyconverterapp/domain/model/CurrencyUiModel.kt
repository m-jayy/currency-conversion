package com.mohsin.mycurrencyconverterapp.domain.model

import java.util.Locale

data class CurrencyUiModel(
    val currencyCode: String,
    val currencyName: String
) : (String) -> Boolean {

    override fun invoke(query: String): Boolean {
        return currencyName.lowercase(Locale.getDefault())
            .contains(query.lowercase(Locale.getDefault()))
    }
}