package com.escrow.wazipay.ui.general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.escrow.wazipay.R
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

@Composable
fun OrdersScreenComposable(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .safeDrawingPadding()
    ) {
        OrdersScreen()
    }
}

@Composable
fun OrdersScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = screenHeight(x = 16.0),
                horizontal = screenWidth(x = 16.0)
            )
    ) {
        Text(
            text = "My Orders",
            fontWeight = FontWeight.Bold,
            fontSize = screenFontSize(x = 16.0).sp
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Completed")
            }
            OutlinedButton(onClick = { /*TODO*/ }) {
                Text(text = "In Transit")
            }
            OutlinedButton(onClick = { /*TODO*/ }) {
                Text(text = "Cancelled")
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        LazyColumn {
            items(10) {
                OrderItem()
            }
        }

    }
}

@Composable
fun OrderItem(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                screenWidth(x = 8.0)
            )
    ) {
        Column(
            modifier = Modifier
                .padding(screenWidth(x = 16.0))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.shop),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                Text(
                    text = "Gas Delivery",
                    fontSize = screenFontSize(x = 16.0).sp
                )
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Row {
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = "6kg Total Gas",
                        fontSize = screenFontSize(x = 16.0).sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1f)
                    )
                    Text(
                        text = "Code: ",
                        fontSize = screenFontSize(x = 16.0).sp
                    )
                    Text(
                        text = "4545",
                        fontSize = screenFontSize(x = 16.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            Text(
                text = "In Transit",
                fontSize = screenFontSize(x = 14.0).sp
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ordered at ",
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Text(
                    text = "12th Dec 2024",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrdersScreenPreview() {
    WazipayTheme {
        OrdersScreen()
    }
}