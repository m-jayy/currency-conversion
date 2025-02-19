package com.mohsin.mycurrencyconverterapp.core.utils

sealed interface SnackBarEvent {
    data object NetworkError : SnackBarEvent
    data object GenericError : SnackBarEvent
}