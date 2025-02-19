package com.mohsin.mycurrencyconverterapp.repositoryTest

import com.mohsin.mycurrencyconverterapp.core.utils.DefaultDispatcherProvider
import com.mohsin.mycurrencyconverterapp.data.db.entity.CurrencyDbModel
import com.mohsin.mycurrencyconverterapp.data.db.entity.ExchangeRatesDbModel
import com.mohsin.mycurrencyconverterapp.data.repository.CurrencyRepositoryImpl
import com.mohsin.mycurrencyconverterapp.testHelper.TestCurrencyLocalDataSource
import com.mohsin.mycurrencyconverterapp.testHelper.TestCurrencyRemoteDataSource
import com.mohsin.mycurrencyconverterapp.testHelper.TestExchangeRatesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CurrencyRepositoryTest {
    private val testDispatcher = StandardTestDispatcher()
    private val currencyRemoteDataSource = TestCurrencyRemoteDataSource()
    private val currencyLocalDataSource = TestCurrencyLocalDataSource()
    private val exchangeRatesDataStore = TestExchangeRatesDataStore()

    private lateinit var repository: CurrencyRepositoryImpl

    @Before
    fun setUp() = runBlocking {
        exchangeRatesDataStore.setLatestBase("USD")
        currencyLocalDataSource.saveCurrencies(emptyList())
        currencyLocalDataSource.saveExchangeRates(emptyList())

        repository = CurrencyRepositoryImpl(
            currencyRemoteDataSource = currencyRemoteDataSource,
            currencyLocalDataSource = currencyLocalDataSource,
            exchangeRatesDataStore = exchangeRatesDataStore,
            dispatcherProvider = DefaultDispatcherProvider(),
        )
    }

    @Test
    fun `Return an empty currencies when no currency is available`() = runTest(testDispatcher) {
        val currencies = repository.currenciesList.first()
        assertEquals(0, currencies.size)
    }

    @Test
    fun `Return currencies if they are available locally`() = runTest(testDispatcher) {
        currencyLocalDataSource.saveCurrencies(currenciesDbModelInput)
        val currencies = repository.currenciesList.first()
        assertEquals(3, currencies.size)
        assertEquals("Japanese Yen", currencies[0].currencyName)
    }

    @Test
    fun `Return empty exchange rate when no data is available`() = runTest(testDispatcher) {
        val exchangeRates = repository.exchangeRatesList.first()
        assertEquals(0, exchangeRates.size)
    }

    @Test
    fun `Return exchange rates if they are available locally`() = runTest(testDispatcher) {
        currencyLocalDataSource.saveExchangeRates(ratesDbModelInput)
        val exchangeRates = repository.exchangeRatesList.first()
        assertEquals(3, exchangeRates.size)
        assertEquals("JPY", exchangeRates.first().currencyCode)
    }

    @Test
    fun `Return an empty list of exchange rates when no base is available`() =
        runTest(testDispatcher) {
            currencyLocalDataSource.saveExchangeRates(
                listOf(
                    ExchangeRatesDbModel("JPY", "", 0.232),
                    ExchangeRatesDbModel("USD", "", 1.312),
                    ExchangeRatesDbModel("EUR", "", 333.3),
                )
            )
            val exchangeRates = repository.exchangeRatesList.first()
            assertEquals(0, exchangeRates.size)
        }

    @Test
    fun `on updateCurrenciesAndRates if the data is not available locally, fetch it from the remote server`() =
        runTest(testDispatcher) {
            // repository is empty
            val currenciesBeforeUpdate = repository.currenciesList.first()
            val exchangeRatesBeforeUpdate = repository.exchangeRatesList.first()

            assertEquals(0, currenciesBeforeUpdate.size)
            assertEquals(0, exchangeRatesBeforeUpdate.size)

            // call updateCurrenciesAndRates
            repository.updateCurrenciesAndRates()

            // repository is not empty
            val currenciesAfterUpdate = repository.currenciesList.first()
            val exchangeRatesAfterUpdate = repository.exchangeRatesList.first()

            assert(currenciesAfterUpdate.isNotEmpty())
            assert(exchangeRatesAfterUpdate.isNotEmpty())

            // Data matches remote data
            assertEquals(currencyRemoteDataSource.currenciesMap.size, currenciesAfterUpdate.size)
            assertEquals(
                currencyRemoteDataSource.getExchangeRatesList().rates.size,
                exchangeRatesAfterUpdate.size
            )

            // Data is added locally
            assert(currencyLocalDataSource.getCurrenciesList().first().isNotEmpty())
            assert(currencyLocalDataSource.getExchangeRatesList("USD").first().isNotEmpty())
        }

    @Test
    fun `If the data is available locally and valid (no older than 30 minutes), fetch it from the db`() =
        runTest(testDispatcher) {
            // Add data locally
            currencyLocalDataSource.saveCurrencies(currenciesDbModelInput)
            currencyLocalDataSource.saveExchangeRates(ratesDbModelInput)

            // Set last update to 10 minutes ago, ensuring it is less than 30 minutes.
            exchangeRatesDataStore.setLastUpdate(System.currentTimeMillis() - 600000)

            repository.updateCurrenciesAndRates()

            val currenciesAfterUpdate = repository.currenciesList.first()
            val exchangeRatesAfterUpdate = repository.exchangeRatesList.first()

            assertEquals(currenciesDbModelInput.size, currenciesAfterUpdate.size)
            assertEquals(ratesDbModelInput.size, exchangeRatesAfterUpdate.size)

            assertEquals(
                currenciesDbModelInput.first().currencyName,
                currenciesAfterUpdate.first().currencyName
            )
            assertEquals(
                ratesDbModelInput.first().currencyCode,
                exchangeRatesAfterUpdate.first().currencyCode
            )
        }

    @Test
    fun `If the data is available locally and not valid (older than 30 minutes), fetch it from the remote server`() =
        runTest(testDispatcher) {
            // Add data locally
            currencyLocalDataSource.saveCurrencies(currenciesDbModelInput)
            currencyLocalDataSource.saveExchangeRates(ratesDbModelInput)

            // Set last update to 40 minutes ago, ensuring it is greater than 30 minutes.
            exchangeRatesDataStore.setLastUpdate(System.currentTimeMillis() - 2400000)

            repository.updateCurrenciesAndRates()

            val currenciesAfterUpdate = repository.currenciesList.first()
            val exchangeRatesAfterUpdate = repository.exchangeRatesList.first()

            assert(currenciesDbModelInput.size != currenciesAfterUpdate.size)
            assert(ratesDbModelInput.size != exchangeRatesAfterUpdate.size)

            assertEquals(
                currencyRemoteDataSource.getCurrenciesList().size,
                currenciesAfterUpdate.size
            )
            assertEquals(
                currencyRemoteDataSource.getExchangeRatesList().rates.size,
                exchangeRatesAfterUpdate.size
            )
        }
}

val currenciesDbModelInput = listOf(
    CurrencyDbModel("JPY", "Japanese Yen"),
    CurrencyDbModel("USD", "United States Dollar"),
    CurrencyDbModel("EUR", "Euro"),
)

val ratesDbModelInput = listOf(
    ExchangeRatesDbModel("JPY", "USD", 0.232),
    ExchangeRatesDbModel("USD", "USD", 1.312),
    ExchangeRatesDbModel("EUR", "USD", 333.3)
)
