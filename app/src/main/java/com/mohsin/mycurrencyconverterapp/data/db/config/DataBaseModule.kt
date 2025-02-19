package com.mohsin.mycurrencyconverterapp.data.db.config

import android.content.Context
import androidx.room.Room
import com.mohsin.mycurrencyconverterapp.core.AppConstants
import com.mohsin.mycurrencyconverterapp.data.db.dao.CurrencyDao
import com.mohsin.mycurrencyconverterapp.data.db.dao.ExchangeRateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Provides
    fun provideCurrencyDao(appDatabase: AppDatabase): CurrencyDao {
        return appDatabase.currencyDao()
    }

    @Provides
    fun provideExchangeRatesDao(appDatabase: AppDatabase): ExchangeRateDao {
        return appDatabase.exchangeRatesDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            AppConstants.DB_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }
}