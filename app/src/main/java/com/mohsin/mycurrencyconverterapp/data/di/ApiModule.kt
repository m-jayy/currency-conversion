package com.mohsin.mycurrencyconverterapp.data.di

import com.mohsin.mycurrencyconverterapp.core.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ApiModule {

    companion object {
        @Provides
        @Singleton
        fun provideRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}