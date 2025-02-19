package com.mohsin.mycurrencyconverterapp.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

const val DropdownBoxTag = "DropdownBox"

@ExperimentalAnimationApi
@Composable
fun <T : DropdownItem> DropdownBox(
    options: List<T>,
    optionContent: @Composable (T) -> Unit,
    dropdownContent: @Composable DropdownScope<T>.() -> Unit
) {
    val dropdownState = remember(options) { DropdownState(initialOptions = options) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        dropdownState.dropdownContent()

        AnimatedVisibility(visible = dropdownState.isSearching) {
            LazyColumn(
                modifier = Modifier
                    .dropdownModifier(dropdownState)
                    .fillMaxWidth(), // Ensuring LazyColumn fills the width
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(dropdownState.filteredOptions, key = { it.hashCode() }) { option ->
                    Box(
                        modifier = Modifier
                            .clickable { dropdownState.selectOption(option) }
                            .padding(8.dp) // Add padding if needed for better touch targets
                    ) {
                        optionContent(option)
                    }
                }
            }
        }
    }
}

private fun Modifier.dropdownModifier(
    dropdownDesignScope: DropdownDesignScope
): Modifier {
    val sizeModifier = if (dropdownDesignScope.shouldWrapHeight) {
        wrapContentHeight()
    } else {
        heightIn(min = 0.dp, max = dropdownDesignScope.maxHeight)
    }

    return sizeModifier
        .testTag(DropdownBoxTag)
        .fillMaxWidth(dropdownDesignScope.widthPercentage)
        .border(
            border = dropdownDesignScope.borderStroke,
            shape = dropdownDesignScope.shape
        )
}