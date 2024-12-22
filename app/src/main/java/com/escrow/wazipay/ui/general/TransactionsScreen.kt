package com.escrow.wazipay.ui.general

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

@Composable
fun TransactionsScreenComposable(
    modifier: Modifier = Modifier
) {

}

@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ElevatedCard(
            shape = RoundedCornerShape(0),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = screenWidth(x = 16.0),
                        vertical = screenHeight(x = 16.0)
                    )
            ) {
                TextField(
                    shape = RoundedCornerShape(screenWidth(x = 10.0)),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    },
                    label = {
                        Text(
                            text = "Search",
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))

            }
        }
        LazyColumn(
            modifier = Modifier
                .padding(
                    horizontal = screenWidth(x = 16.0),
                    vertical = screenWidth(x = 16.0)
                )
        ) {
            items(20) {
                TransactionCellComposable(
                    modifier = Modifier
                        .padding(
                            top = screenHeight(x = 8.0)
                        )
                )
            }
        }
    }
}

@Composable
fun TransactionCellComposable(
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = { /*TODO*/ },
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(screenWidth(x = 8.0))
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "6KG Total Gas Delivery",
                    fontWeight = FontWeight.Bold,
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Text(
                    text = "PAYMENT",
                    fontWeight = FontWeight.Bold,
                    fontSize = screenFontSize(x = 16.0).sp
                )
                Text(
                    text = "20/12/2024",
                    fontWeight = FontWeight.W300,
                    fontSize = screenFontSize(x = 12.0).sp
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "-1500.00",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    fontSize = screenFontSize(x = 16.0).sp
                )
                Text(
                    text = "KES",
                    fontWeight = FontWeight.W300,
                    fontSize = screenFontSize(x = 12.0).sp
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TransactionsScreenPreview() {
    WazipayTheme {
        TransactionsScreen()
    }
}