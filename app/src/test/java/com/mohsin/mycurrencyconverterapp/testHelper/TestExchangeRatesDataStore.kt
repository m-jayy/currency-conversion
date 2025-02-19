package com.mohsin.mycurrencyconverterapp.testHelper

import com.mohsin.mycurrencyconverterapp.data.dataSources.ExchangeRatesDataStore
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow

class TestExchangeRatesDataStore: ExchangeRatesDataStore {

    private val latestBaseFlow: MutableSharedFlow<String> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private var lastUpdateFlow: Long? = null

    override val latestBase: Flow<String>
        get() = latestBaseFlow

    override val lastUpdate: Flow<Long?>
        get() = flow { lastUpdateFlow?.let { emit(it) } }


    override suspend fun setLastUpdate(lastUpdate: Long) {
        lastUpdateFlow = lastUpdate
    }

    override suspend fun setLatestBase(base: String) {
        latestBaseFlow.emit(base)
    }
}