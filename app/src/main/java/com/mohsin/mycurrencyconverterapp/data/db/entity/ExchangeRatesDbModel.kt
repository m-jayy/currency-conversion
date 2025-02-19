package com.mohsin.mycurrencyconverterapp.data.db.entity

import androidx.room.Entity

@Entity(tableName = "exchange_rate", primaryKeys = ["currencyCode", "base"])
data class ExchangeRatesDbModel(
    val currencyCode: String,
    val base: String,
    val rate: Double
)
