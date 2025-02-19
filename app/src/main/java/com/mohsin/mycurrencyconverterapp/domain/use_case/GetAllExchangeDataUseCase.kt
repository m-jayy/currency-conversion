package com.mohsin.mycurrencyconverterapp.domain.use_case

import com.mohsin.mycurrencyconverterapp.domain.model.ExchangeResultUiModel
import com.mohsin.mycurrencyconverterapp.domain.repository.CurrencyRepository
import com.mohsin.mycurrencyconverterapp.presentation.theme.AppStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.DecimalFormat
import javax.inject.Inject

class GetAllExchangeDataUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    private val amountFormat = DecimalFormat(AppStyle.DECIMAL_FORMAT)

    operator fun invoke(source: String?, amount: Double) : Flow<List<ExchangeResultUiModel>>{
        return currencyRepository.exchangeRatesList.map { list ->
            list.firstOrNull { it.currencyCode == source }?.let { sourceCurrency ->
                val rateSource = sourceCurrency.rate
                val inBase = amount / rateSource
                list.map {
                    val rateResult = it.rate * inBase
                    ExchangeResultUiModel(
                        currencyCode = it.currencyCode,
                        amount = amountFormat.format(rateResult)
                    )
                }
            } ?: emptyList()
        }}

}