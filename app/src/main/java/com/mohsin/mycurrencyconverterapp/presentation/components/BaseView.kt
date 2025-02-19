package com.mohsin.mycurrencyconverterapp.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseView(
    modifier: Modifier = Modifier,
    title: String,
    snackBarHostState: SnackbarHostState? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        },
        snackbarHost = {
            snackBarHostState?.let { SnackbarHost(it) }
        },
        content = content
    )
}

