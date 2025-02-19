package com.mohsin.mycurrencyconverterapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class CurrencyDbModel(
    @PrimaryKey val currencyCode: String,
    val currencyName: String
)