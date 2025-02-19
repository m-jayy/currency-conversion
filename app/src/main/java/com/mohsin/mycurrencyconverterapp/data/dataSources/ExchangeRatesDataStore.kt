package com.mohsin.mycurrencyconverterapp.data.dataSources

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mohsin.mycurrencyconverterapp.core.AppConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ExchangeRatesDataStore {
    val lastUpdate: Flow<Long?>
    val latestBase: Flow<String>
    suspend fun setLastUpdate(lastUpdate: Long)
    suspend fun setLatestBase(base: String)
}

class ExchangeRatesDataStoreImpl @Inject constructor(
    private val appContext: Context
): ExchangeRatesDataStore {

    private val Context.dataStore by preferencesDataStore(AppConstants.PREFERENCE_DATASTORE_KEY)

    private val lastUpdateKey = longPreferencesKey(AppConstants.LAST_UPDATE_KEY)
    private val latestBaseKey = stringPreferencesKey(AppConstants.LATEST_BASE_KEY)

    override val lastUpdate: Flow<Long?> = appContext.dataStore.data.map { preferences ->
        preferences[lastUpdateKey]
    }
    override val latestBase: Flow<String> = appContext.dataStore.data.map { preferences ->
        preferences[latestBaseKey] ?: AppConstants.DEFAULT_BASE_CURRENCY
    }

    override suspend fun setLastUpdate(lastUpdate: Long) {
        appContext.dataStore.edit { preferences ->
            preferences[lastUpdateKey] = lastUpdate
        }
    }

    override suspend fun setLatestBase(base: String) {
        appContext.dataStore.edit { preferences ->
            preferences[latestBaseKey] = base
        }
    }
}