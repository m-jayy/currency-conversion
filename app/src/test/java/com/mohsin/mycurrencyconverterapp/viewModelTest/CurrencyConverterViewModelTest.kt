package com.mohsin.mycurrencyconverterapp.viewModelTest

import app.cash.turbine.test
import com.mohsin.mycurrencyconverterapp.core.utils.SnackBarEvent
import com.mohsin.mycurrencyconverterapp.domain.model.CurrencyUiModel
import com.mohsin.mycurrencyconverterapp.domain.model.ExchangeRateUIModel
import com.mohsin.mycurrencyconverterapp.domain.use_case.GetAllCurrenciesUseCase
import com.mohsin.mycurrencyconverterapp.domain.use_case.GetAllExchangeDataUseCase
import com.mohsin.mycurrencyconverterapp.presentation.views.currency_converter.CurrencyConverterViewModel
import com.mohsin.mycurrencyconverterapp.testHelper.MainCoroutineRule
import com.mohsin.mycurrencyconverterapp.testHelper.TestRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CurrencyConverterViewModelTest {


    private val currencyRepository = TestRepository()

    private val getAllCurrenciesUseCase = GetAllCurrenciesUseCase(currencyRepository = currencyRepository)
    private val getAllExchangeDataUseCase = GetAllExchangeDataUseCase(currencyRepository = currencyRepository)

    // ViewModel instance
    private lateinit var viewModel: CurrencyConverterViewModel

    // Coroutine test rule
    @get:Rule
    val coroutineTestRule = MainCoroutineRule()

    @Before
    fun setUp() {
        viewModel = CurrencyConverterViewModel(
            getAllCurrenciesUseCase = getAllCurrenciesUseCase,
            getAllExchangeDataUseCase = getAllExchangeDataUseCase
        )
    }

    @Test
    fun `updateAmount should update currentAmountFlow`() = runTest {
        // Act
        viewModel.updateAmount("100")

        // Assert
        assertEquals(100, viewModel.currentAmountState.value)
    }

    @Test
    fun `updateAmount with invalid input should not update currentAmountFlow`() = runTest {
        // Act
        viewModel.updateAmount("invalid")

        // Assert
        assertEquals(0, viewModel.currentAmountState.value) // Default value
    }

    @Test
    fun `updateSourceCurrency should update selectedCurrencyFlow`() = runTest {
        // Arrange
        val currency = CurrencyUiModel(currencyCode = "USD", currencyName = "US Dollar")

        // Act
        viewModel.updateSourceCurrency(currency)

        // Assert
        assertEquals(currency, viewModel.selectedCurrencyState.value)
    }

    @Test
    fun `when updateCurrenciesAndRates fails, NetworkError should be emitted for allCurrencies`() = runTest {
        currencyRepository.setShouldThrowsError(true)
        viewModel.snackBarEvent.test {
            viewModel.allCurrencies.first() // Ensure the flow emits something
            val emission = awaitItem() // Collect and assert the SnackBarEvent emission
            assertEquals(SnackBarEvent.NetworkError, emission)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Should get currencies when available`() = runTest {
        val currencies = listOf(
            CurrencyUiModel("USD", "US Dollar"),
            CurrencyUiModel("JPY", "Japanese Yen"),
            CurrencyUiModel("PKR", "Pakistani Rupee")
        )
        currencyRepository.sendCurrenciesModel(currencies)

        val allCurrencies = viewModel.allCurrencies.first()
        assertEquals(currencies.size, allCurrencies.size)
        assertEquals(currencies.first().currencyCode, allCurrencies.first().currencyCode)
    }

    @Test
    fun `Should get empty currencies when not available locally`() = runTest {
        val allCurrencies = viewModel.allCurrencies.first()
        assertEquals(0, allCurrencies.size)
    }

    @Test
    fun `Should get empty list when amount is smaller than 0`() = runTest {
        currencyRepository.sendCurrencyRatesModel(ratesInput)
        viewModel.updateSourceCurrency(CurrencyUiModel("USD", "USD - usd"))
        viewModel.updateAmount("-1")

        assert(viewModel.currenciesExchangeResults.first().isEmpty())
    }

    @Test
    fun `Should get non-empty list when amount is greater than 0`() = runTest {
        currencyRepository.sendCurrencyRatesModel(ratesInput)
        viewModel.updateSourceCurrency(CurrencyUiModel("USD", "USD - usd"))
        viewModel.updateAmount("1000")

        assert(viewModel.currenciesExchangeResults.first().isNotEmpty())
    }
}

val ratesInput = listOf(
    ExchangeRateUIModel("JPY", "USD", 110.5),
    ExchangeRateUIModel("PKR", "USD", 285.0),
    ExchangeRateUIModel("USD", "USD", 1.0)
)
