package com.mohsin.mycurrencyconverterapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mohsin.mycurrencyconverterapp.data.db.entity.CurrencyDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currency order by currencyCode asc")
    fun getAll(): Flow<List<CurrencyDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(currencies: List<CurrencyDbModel>)
}