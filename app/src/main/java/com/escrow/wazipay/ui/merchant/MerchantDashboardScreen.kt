package com.escrow.wazipay.ui.merchant

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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.transaction.transactions
import com.escrow.wazipay.ui.general.TransactionCellComposable
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

@Composable
fun MerchantDashboardScreenComposable(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .safeDrawingPadding()
    ) {
        MerchantDashboardScreen()
    }
}

@Composable
fun MerchantDashboardScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = screenWidth(x = 16.0),
                vertical = screenHeight(x = 16.0)
            )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(screenWidth(x = 16.0))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.person),
                        contentDescription = null
                    )
                    Text(
                        text = "Alex".uppercase(),
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    ElevatedCard {
                        Text(
                            text = "VERIFIED",
                            fontSize = screenFontSize(x = 12.0).sp,
                            color = Color(0xFF0d421b),
                            modifier = Modifier
                                .padding(screenWidth(x = 3.0))
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "MERCHANT",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Text(
                    text = "Wallet Balance",
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Text(
                    text = "KES 1000.00",
                    fontWeight = FontWeight.Bold,
                    fontSize = screenFontSize(x = 24.0).sp
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { /*TODO*/ }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "+",
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Text(
                                text = "Deposit",
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    OutlinedButton(onClick = { /*TODO*/ }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "-",
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Text(
                                text = "Withdraw",
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hide Balance",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Switch(checked = false, onCheckedChange = {})
                }
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = "Recent Transactions",
            fontSize = screenFontSize(x = 16.0).sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        LazyColumn {
            items(transactions) { transaction ->
                TransactionCellComposable(
                    transactionData = transaction,
                    modifier = Modifier
                        .padding(
                            top = screenHeight(x = 8.0)
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MerchantDashboardScreenPreview() {
    WazipayTheme {
        MerchantDashboardScreen()
    }
}