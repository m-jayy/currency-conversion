package com.mohsin.mycurrencyconverterapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mohsin.mycurrencyconverterapp.data.db.entity.ExchangeRatesDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRateDao {
    @Query("SELECT * FROM exchange_rate WHERE base = :base ORDER BY currencyCode asc")
    fun getAllByBase(base: String): Flow<List<ExchangeRatesDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(exchangeRates: List<ExchangeRatesDbModel>)
}