package com.mohsin.mycurrencyconverterapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mohsin.mycurrencyconverterapp.presentation.theme.Red40


private typealias OnOptionSelected<T> = (T) -> Unit

@Stable
interface DropdownScope<T : DropdownItem> : DropdownDesignScope {
    var isSearching: Boolean
    fun filterOptions(query: String)
    fun onOptionSelected(block: OnOptionSelected<T> = {})
}

@Stable
interface DropdownDesignScope {
    var widthPercentage: Float
    var shouldWrapHeight: Boolean
    var maxHeight: Dp
    var borderStroke: BorderStroke
    var shape: Shape
}

class DropdownState<T : DropdownItem>(private val initialOptions: List<T>) :
    DropdownScope<T> {
    private var onOptionSelectedBlock: OnOptionSelected<T>? = null

    fun selectOption(option: T) {
        onOptionSelectedBlock?.invoke(option)
    }

    var filteredOptions by mutableStateOf(initialOptions)
    override var isSearching by mutableStateOf(false)
    override var widthPercentage by mutableFloatStateOf(1f)
    override var shouldWrapHeight by mutableStateOf(false)
    override var maxHeight: Dp by mutableStateOf(TextFieldDefaults.MinHeight * 4)
    override var borderStroke by mutableStateOf(BorderStroke(1.dp, Red40))
    override var shape: Shape by mutableStateOf(RoundedCornerShape(8.dp))

    override fun filterOptions(query: String) {
        filteredOptions = initialOptions.filter { item ->
            item(query)
        }
    }

    override fun onOptionSelected(block: OnOptionSelected<T>) {
        onOptionSelectedBlock = block
    }
}
