package com.mohsin.mycurrencyconverterapp.domain.use_case

import com.mohsin.mycurrencyconverterapp.domain.model.CurrencyUiModel
import com.mohsin.mycurrencyconverterapp.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllCurrenciesUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    // Function to get the list of currencies
    operator fun invoke(): Flow<List<CurrencyUiModel>> =
        currencyRepository.currenciesList.map { list ->
            list.map {
                CurrencyUiModel(
                    currencyCode = it.currencyCode,
                    currencyName = "${it.currencyCode} - ${it.currencyName}"
                )
            }
        }

    // Function to update currencies and their rates
    suspend fun updateCurrenciesAndRates() {
        currencyRepository.updateCurrenciesAndRates()
    }
}