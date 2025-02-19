package com.mohsin.mycurrencyconverterapp.usecase

import com.mohsin.mycurrencyconverterapp.domain.model.CurrencyUiModel
import com.mohsin.mycurrencyconverterapp.domain.use_case.GetAllCurrenciesUseCase
import com.mohsin.mycurrencyconverterapp.testHelper.TestRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAllCurrenciesUseCaseTest {

    private val currencyRepository = TestRepository()
    private val useCase = GetAllCurrenciesUseCase(currencyRepository)

    @Test
    fun `Fetch currencies from repository`() = runBlocking {
        // Arrange
        val currenciesInput = listOf(
            CurrencyUiModel("AED", "United Arab Emirates"),
            CurrencyUiModel("AFN", "Afghan Afghani"),
            CurrencyUiModel("ALL", "Albanian Lek"),
        )
        currencyRepository.sendCurrenciesModel(currenciesInput)

        // Act
        val result = useCase().first()

        // Assert
        assertEquals(currenciesInput.size, result.size)
    }

    @Test
    fun `Return an empty list when no currencies are available`() = runBlocking {
        // Arrange
        currencyRepository.sendCurrenciesModel(emptyList())

        // Act
        val result = useCase().first()

        // Assert
        assertEquals(0, result.size)
        assert(result.isEmpty())
    }

    @Test
    fun `Data should update when it is invalid`() = runBlocking {
        // Arrange
        currencyRepository.setShouldThrowsError(false)

        // Act
        useCase.updateCurrenciesAndRates()

        // Assert
        assertEquals(true, currencyRepository.hasTriedUpdateCurrenciesAndRates)
    }
}
