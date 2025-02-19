package com.mohsin.mycurrencyconverterapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mohsin.mycurrencyconverterapp.domain.model.ExchangeResultUiModel
import com.mohsin.mycurrencyconverterapp.presentation.theme.AppStyle
import com.mohsin.mycurrencyconverterapp.presentation.theme.MyCurrencyConverterAppTheme
import com.mohsin.mycurrencyconverterapp.presentation.theme.Red40
import com.mohsin.mycurrencyconverterapp.presentation.theme.Red80

@Composable
fun ContainerExchangeCard(
    modifier: Modifier = Modifier,
    item: ExchangeResultUiModel
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppStyle.CardPadding)
            .background(Red80, shape = RoundedCornerShape(AppStyle.RoundedCorner)),
        shape = RoundedCornerShape(AppStyle.RoundedCorner),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppStyle.CardPadding),
        ) {
            Text(
                text = item.currencyCode,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Red80
            )

            Spacer(modifier = Modifier.size(AppStyle.defaultSpacerSize))

            Text(
                text = item.amount,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Red40,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    }

@Preview(widthDp = 300)
@Composable
private fun ExchangeResultPreview() {
    MyCurrencyConverterAppTheme {
        ContainerExchangeCard(
            item = ExchangeResultUiModel("PKR", "123,123.00")
        )
    }
}
