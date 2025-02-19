package com.mohsin.mycurrencyconverterapp.presentation.views.currency_converter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohsin.mycurrencyconverterapp.R
import com.mohsin.mycurrencyconverterapp.core.AppConstants
import com.mohsin.mycurrencyconverterapp.core.utils.AmountFormater
import com.mohsin.mycurrencyconverterapp.core.utils.SnackBarEvent
import com.mohsin.mycurrencyconverterapp.domain.model.CurrencyUiModel
import com.mohsin.mycurrencyconverterapp.domain.model.ExchangeResultUiModel
import com.mohsin.mycurrencyconverterapp.presentation.theme.MyCurrencyConverterAppTheme
import com.mohsin.mycurrencyconverterapp.presentation.components.BaseView
import com.mohsin.mycurrencyconverterapp.presentation.components.ContainerExchangeCard
import com.mohsin.mycurrencyconverterapp.presentation.components.DropdownBox
import com.mohsin.mycurrencyconverterapp.presentation.components.TextSearchBar
import com.mohsin.mycurrencyconverterapp.presentation.theme.AppStyle
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CurrencyConvertView : ComponentActivity() {
    private val viewModel: CurrencyConverterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyConverterScreen(viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CurrencyConverterScreen(viewModel: CurrencyConverterViewModel) {
    val allAvailableCurrenciesState by viewModel.allCurrencies.collectAsStateWithLifecycle()
    val currenciesExchangeResultsState by viewModel.currenciesExchangeResults.collectAsStateWithLifecycle()
    val currentAmountState by viewModel.currentAmountState.collectAsStateWithLifecycle()
    val selectedCurrencyState by viewModel.selectedCurrencyState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackBarEvent.collect {
            val snackBarMessage = when (it) {
                SnackBarEvent.NetworkError -> "Network Error"
                else -> "Something went wrong"
            }

            if (snackBarMessage.isNotEmpty()) {
                snackBarHostState.showSnackbar(
                    message = snackBarMessage,
                    duration = SnackbarDuration.Long,
                )
            }
        }
    }


    MainCurrencyConverterScreen(
        title = stringResource(R.string.app_title),
        snackBarHostState = snackBarHostState,
        currentAmount = currentAmountState,
        selectedCurrency = selectedCurrencyState,
        allAvailableCurrencies = allAvailableCurrenciesState,
        currenciesExchangeResults = currenciesExchangeResultsState,
        onAmountUpdated = { newAmount ->
            viewModel.updateAmount(newAmount)
        },
        onCurrencyUpdated = { newCurrency ->
            viewModel.updateSourceCurrency(newCurrency)
        }
    )
}

@ExperimentalAnimationApi
@Composable
private fun MainCurrencyConverterScreen(
    title: String,
    snackBarHostState: SnackbarHostState?,
    currentAmount: Int,
    selectedCurrency: CurrencyUiModel?,
    allAvailableCurrencies: List<CurrencyUiModel>,
    currenciesExchangeResults: List<ExchangeResultUiModel>,
    onAmountUpdated: (String) -> Unit,
    onCurrencyUpdated: (CurrencyUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    MyCurrencyConverterAppTheme {
        BaseView(
            title = title,
            modifier = modifier.fillMaxSize(),
            snackBarHostState = snackBarHostState,
            content = {
                Column(
                    modifier = modifier.padding(
                        start = AppStyle.defaultColumnPadding.calculateStartPadding(LayoutDirection.Ltr),
                        top = it.calculateTopPadding(),
                        end = AppStyle.defaultColumnPadding.calculateEndPadding(LayoutDirection.Ltr)
                    )
                ) {
                    EditTextAmount(currentAmount, onAmountUpdated)
                    Spacer(modifier = modifier.size(AppStyle.defaultSpacerSize))
                    TextFieldDropdownCurrency(
                        selectedCurrency,
                        allAvailableCurrencies,
                        onCurrencyUpdated
                    )
                    if (selectedCurrency != null && currentAmount >= 0) {
                        GridExchangeResults(currenciesExchangeResults)
                    }
                }
            }
        )
    }
}

@Composable
private fun EditTextAmount(
    currentAmount: Int,
    onAmountUpdated: (String) -> Unit
) {
    var amountState by remember {
        mutableStateOf(if (currentAmount == 0) "" else currentAmount.toString())
    }
    LaunchedEffect(amountState) {
        onAmountUpdated(amountState)
    }
    Box(modifier = Modifier) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .then(AppStyle.RoundedCard),
            value = amountState,
            onValueChange = { newValue ->
                if (newValue.length <= 9 && newValue.matches("^[0-9]*$".toRegex())) {
                    amountState = newValue
                }
            },
            visualTransformation = AmountFormater(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            label = { Text(stringResource(R.string.amount)) },
            singleLine = true,
            trailingIcon = {
                if (amountState.isNotEmpty()) {
                    IconButton(onClick = { amountState = "0" }) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                    }
                }
            },
        )
    }
}

@ExperimentalAnimationApi
@Composable
private fun TextFieldDropdownCurrency(
    currentCurrency: CurrencyUiModel?,
    currencies: List<CurrencyUiModel>,
    onCurrencyUpdated: (CurrencyUiModel) -> Unit
) {
    DropdownBox(
        options = currencies,
        optionContent = { currency ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = AppStyle.defaultHorizontalMargin,
                        vertical = AppStyle.defaultVerticalMargin
                    ),
                text = currency.currencyName
            )
        }
    ) {
        var selectedValue by remember { mutableStateOf(currentCurrency?.currencyName.orEmpty()) }
        val view = LocalView.current

        onOptionSelected { currency ->
            onCurrencyUpdated(currency)
            selectedValue = currency.currencyName
            filterOptions(selectedValue)
            view.clearFocus()
        }

        TextSearchBar(
            modifier = Modifier.fillMaxWidth(),
            value = selectedValue,
            label = stringResource(R.string.select_currency),
            onDoneActionClick = {
                view.clearFocus()
            },
            onClearClick = {
                selectedValue = ""
                filterOptions(selectedValue)
                view.clearFocus()

                onCurrencyUpdated(
                    CurrencyUiModel(
                        currencyCode = "",
                        currencyName = ""
                    )
                )
            },
            onFocusChanged = { focusState ->
                isSearching = focusState.isFocused
            },
            onValueChanged = { query ->
                selectedValue = query
                filterOptions(selectedValue)
            }
        )
    }
}

@Composable
private fun GridExchangeResults(
    results: List<ExchangeResultUiModel>
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = AppStyle.defaultVerticalMargin),
        columns = GridCells.Fixed(AppConstants.GRID_COUNT),
        contentPadding = AppStyle.defaultContentPadding,
    ) {
        items(results) { item ->
            ContainerExchangeCard(
                item = item
            )
        }
    }
}