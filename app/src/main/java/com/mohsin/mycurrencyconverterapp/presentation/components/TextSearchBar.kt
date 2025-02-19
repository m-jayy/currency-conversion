@file:OptIn(ExperimentalMaterial3Api::class)

package com.mohsin.mycurrencyconverterapp.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.mohsin.mycurrencyconverterapp.presentation.theme.AppStyle

@Composable
fun TextSearchBar(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onFocusChanged: (FocusState) -> Unit = {},
    onValueChanged: (String) -> Unit
) {
    TextField(
        modifier =
        modifier
            .fillMaxWidth()
            .then(AppStyle.RoundedCard)
            .onFocusChanged(onFocusChanged),
        value = value,
        onValueChange = onValueChanged,
        label = { Text(text = label) },
        textStyle = MaterialTheme.typography.bodyMedium,
        singleLine = true,
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = onClearClick) {
                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                }
            }
        },
        keyboardActions = KeyboardActions(onDone = { onDoneActionClick() }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        )
    )
}
