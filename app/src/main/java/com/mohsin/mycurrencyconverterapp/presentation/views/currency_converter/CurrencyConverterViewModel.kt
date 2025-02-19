package com.mohsin.mycurrencyconverterapp.presentation.views.currency_converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohsin.mycurrencyconverterapp.core.AppConstants
import com.mohsin.mycurrencyconverterapp.core.utils.SnackBarEvent
import com.mohsin.mycurrencyconverterapp.domain.model.CurrencyUiModel
import com.mohsin.mycurrencyconverterapp.domain.model.ExchangeResultUiModel
import com.mohsin.mycurrencyconverterapp.domain.use_case.GetAllCurrenciesUseCase
import com.mohsin.mycurrencyconverterapp.domain.use_case.GetAllExchangeDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
    getAllExchangeDataUseCase: GetAllExchangeDataUseCase,
) : ViewModel() {

    private val selectedCurrencyFlow = MutableStateFlow<CurrencyUiModel?>(null)
    val selectedCurrencyState: StateFlow<CurrencyUiModel?> = selectedCurrencyFlow

    private val currentAmountFlow = MutableStateFlow(0)
    val currentAmountState: StateFlow<Int> = currentAmountFlow

    private val snackBarEventSharedFlow = MutableSharedFlow<SnackBarEvent>()
    val snackBarEvent: SharedFlow<SnackBarEvent> = snackBarEventSharedFlow


    val allCurrencies: StateFlow<List<CurrencyUiModel>> = getAllCurrenciesUseCase()
        .onStart {
            viewModelScope.launch {
                try {
                    getAllCurrenciesUseCase.updateCurrenciesAndRates()
                } catch (e: Exception) {
                    snackBarEventSharedFlow.emit(SnackBarEvent.NetworkError)
                }

            }
        }
        .catch {
            snackBarEventSharedFlow.emit(SnackBarEvent.GenericError)
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(AppConstants.FLOW_RETAINED_TIME),
            initialValue = emptyList()
        )


    @OptIn(ExperimentalCoroutinesApi::class)
    val currenciesExchangeResults: StateFlow<List<ExchangeResultUiModel>> =
        selectedCurrencyFlow.combine(currentAmountFlow) { source, amount ->
            return@combine Pair(source, amount)
        }
            .filter {
                it.first != null && it.second >= 0
            }
            .flatMapLatest {
                getAllExchangeDataUseCase(it.first?.currencyCode, it.second.toDouble())
            }
            .catch {
                snackBarEventSharedFlow.emit(SnackBarEvent.GenericError)
                emit(emptyList())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(AppConstants.FLOW_RETAINED_TIME),
                initialValue = emptyList()
            )

    fun updateAmount(amount: String) {
        amount.toIntOrNull()?.let {
            currentAmountFlow.value = it
        } ?: run {
            currentAmountFlow.value = 0
        }
    }

    fun updateSourceCurrency(currency: CurrencyUiModel) {
        if (currency.currencyName == "")
            selectedCurrencyFlow.value = null
        else
            selectedCurrencyFlow.value = currency
    }
}