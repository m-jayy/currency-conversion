package com.mohsin.mycurrencyconverterapp.data.db.config

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mohsin.mycurrencyconverterapp.data.db.dao.CurrencyDao
import com.mohsin.mycurrencyconverterapp.data.db.dao.ExchangeRateDao
import com.mohsin.mycurrencyconverterapp.data.db.entity.CurrencyDbModel
import com.mohsin.mycurrencyconverterapp.data.db.entity.ExchangeRatesDbModel


@Database(
    exportSchema = true,
    entities = [
        CurrencyDbModel::class,
        ExchangeRatesDbModel::class,
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun currencyDao(): CurrencyDao
    abstract fun exchangeRatesDao(): ExchangeRateDao
}