package com.mohsin.mycurrencyconverterapp.usecase

import com.mohsin.mycurrencyconverterapp.domain.model.ExchangeRateUIModel
import com.mohsin.mycurrencyconverterapp.domain.use_case.GetAllExchangeDataUseCase
import com.mohsin.mycurrencyconverterapp.testHelper.TestRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAllExchangeDataUseCaseTest {

    private val currencyRepository = TestRepository()
    private val useCase = GetAllExchangeDataUseCase(currencyRepository)

    @Test
    fun `Fetch exchange rate from repository`() = runBlocking {
        // Arrange
        currencyRepository.sendCurrencyRatesModel(ratesInput)

        // Act
        val result = useCase("JPY", 100.0).first()

        // Assert
        assertEquals(ratesInput.size, result.size)
    }

    @Test
    fun `Should return empty if source currency is invalid`() = runBlocking {
        // Arrange
        currencyRepository.sendCurrencyRatesModel(ratesInput)

        // Act
        val result = useCase("INVALID", 100.0).first()

        // Assert
        assert(result.isEmpty())
    }

    @Test
    fun `Return an empty result when no exchange rates are available`() = runBlocking {
        // Arrange
        currencyRepository.sendCurrencyRatesModel(emptyList())

        // Act
        val result = useCase("USD", 100.0).first()

    }
}


val ratesInput = listOf(
    ExchangeRateUIModel("JPY", "USD", 110.5),
    ExchangeRateUIModel("PKR", "USD", 285.0),
    ExchangeRateUIModel("USD", "USD", 1.0)
)