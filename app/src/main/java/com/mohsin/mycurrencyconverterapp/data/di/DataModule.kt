package com.mohsin.mycurrencyconverterapp.data.di

import android.content.Context
import com.mohsin.mycurrencyconverterapp.core.utils.DefaultDispatcherProvider
import com.mohsin.mycurrencyconverterapp.core.utils.DispatcherProvider
import com.mohsin.mycurrencyconverterapp.data.dataSources.CurrencyLocalDataSource
import com.mohsin.mycurrencyconverterapp.data.dataSources.CurrencyLocalDataSourceImpl
import com.mohsin.mycurrencyconverterapp.data.dataSources.CurrencyRemoteDataSource
import com.mohsin.mycurrencyconverterapp.data.dataSources.CurrencyRemoteDataSourceImpl
import com.mohsin.mycurrencyconverterapp.data.dataSources.ExchangeRatesDataStore
import com.mohsin.mycurrencyconverterapp.data.dataSources.ExchangeRatesDataStoreImpl
import com.mohsin.mycurrencyconverterapp.data.repository.CurrencyRepositoryImpl
import com.mohsin.mycurrencyconverterapp.domain.repository.CurrencyRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindCurrencyRemoteDataSource(currencyRemoteDataSourceImpl: CurrencyRemoteDataSourceImpl): CurrencyRemoteDataSource

    @Singleton
    @Binds
    fun bindCurrencyRepository(currencyRepositoryImpl: CurrencyRepositoryImpl): CurrencyRepository

    @Singleton
    @Binds
    fun bindCurrencyLocalDataSource(currencyLocalDataSourceImpl: CurrencyLocalDataSourceImpl): CurrencyLocalDataSource


    companion object {

        @Singleton
        @Provides
        fun provideExchangeRateDataStore(@ApplicationContext appContext: Context): ExchangeRatesDataStore {
            return ExchangeRatesDataStoreImpl(appContext)
        }

        @Singleton
        @Provides
        fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()

    }
}