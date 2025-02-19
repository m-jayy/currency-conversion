package com.mohsin.mycurrencyconverterapp.presentation.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object AppStyle {
    // Default paddings
    val defaultColumnPadding = PaddingValues(start = 16.dp, end = 16.dp)
    val defaultContentPadding = PaddingValues(vertical = 4.dp, horizontal = 4.dp)

    // Sizes
    val defaultSpacerSize: Dp = 8.dp

    // Margins
    val defaultHorizontalMargin: Dp = 8.dp
    val defaultVerticalMargin: Dp = 12.dp

    //grid design
    val CardPadding: Dp = 8.dp
    val RoundedCorner: Dp = 12.dp

    const val DECIMAL_FORMAT: String = "#,###.##"

    // Modifier for rounded corners with stroke (border)
    val RoundedCard: Modifier = Modifier
        .clip(RoundedCornerShape(16.dp)) // Rounded corners
        .border(
            width = 1.dp,
            color = Red80,
            shape = RoundedCornerShape(16.dp)
        )
}