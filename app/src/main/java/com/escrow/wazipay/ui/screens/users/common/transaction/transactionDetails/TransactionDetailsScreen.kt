package com.escrow.wazipay.ui.screens.users.common.transaction.transactionDetails

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.transaction.TransactionData
import com.escrow.wazipay.data.network.models.transaction.transactionData
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.formatIsoDateTime
import com.escrow.wazipay.utils.formatIsoDateTime2
import com.escrow.wazipay.utils.formatMoneyValue
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth
import java.time.LocalDateTime

@Composable
fun TransactionDetailsScreenComposable(
    modifier: Modifier = Modifier
) {

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionDetailsScreen(
    userId: Int,
    transactionData: TransactionData,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {

    val sender = when(transactionData.transactionType) {
        "WALLET_DEPOSIT" -> "You"
        "WALLET_WITHDRAWAL" -> "Wazipay Escrow"
        "ESCROW_PAYMENT" -> {
            if(transactionData.order != null) {
                if(transactionData.order.buyer!!.id == userId) {
                    "You"
                } else {
                    ""
                }
            } else {
                "You"
            }
        }
        "ESCROW_RELEASE" -> "Wazipay Escrow"
        "ESCROW_REFUND_TO_BUYER" -> "Wazipay Escrow"
        "MERCHANT_REFUND_TO_BUYER" -> transactionData.order!!.merchant!!.username
        "COURIER_PAYMENT" -> {
            if(transactionData.order!!.courier!!.id == userId) {
                transactionData.order.merchant!!.username
            } else {
                "You"
            }
        }
        else -> "N/A"
    }

    val recipient = when(transactionData.transactionType) {
        "WALLET_DEPOSIT" -> "Your Wazipay wallet"
        "WALLET_WITHDRAWAL" -> "Your M-PESA"
        "ESCROW_PAYMENT" -> {
            if(transactionData.order != null) {
                if(transactionData.order.merchant!!.id == userId) {
                    ""
                } else {
                    ""
                }
            } else {
                "You"
            }
        }
        "ESCROW_RELEASE" -> "Wazipay Escrow"
        "ESCROW_REFUND_TO_BUYER" -> {
            if(transactionData.order!!.buyer!!.id == userId) {
                "Your Wazipay wallet"
            } else {
                ""
            }
        }
        "MERCHANT_REFUND_TO_BUYER" -> {
            if(transactionData.order!!.buyer!!.id == userId) {
                "Your Wazipay wallet"
            } else {
                ""
            }
        }
        "COURIER_PAYMENT" -> {
            if(transactionData.order!!.courier!!.id == userId) {
                "You"
            } else {
                ""
            }
        }
        else -> "N/A"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = screenHeight(x = 16.0),
                horizontal = screenWidth(x = 16.0)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = navigateToPreviousScreen
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous screen",
                    modifier = Modifier
                        .size(screenWidth(x = 24.0))
                )
            }
            Spacer(modifier = Modifier.width(screenWidth(x = 5.0)))
            Text(
                text = "Transaction details",
                fontSize = screenFontSize(x = 14.0).sp
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Image(
                painter = painterResource(id = R.drawable.checked),
                contentDescription = null,
                modifier = Modifier
                    .size(screenWidth(x = 40.0))
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Text(
                text = "Transaction Completed!",
                fontSize = screenFontSize(x = 14.0).sp
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Text(
                text = formatMoneyValue(transactionData.amount),
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(screenWidth(x = 16.0))
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Transaction type",
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
//                                .weight(1f)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = transactionData.transactionType?.lowercase()?.replace("_", " ")?.replaceFirstChar { it.uppercase() } ?: "N/A",
                            fontSize = screenFontSize(x = 14.0).sp,
                            modifier = Modifier
//                                .weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Sender",
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
//                                .weight(1f)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = sender,
                            fontSize = screenFontSize(x = 14.0).sp,
                            modifier = Modifier
//                                .weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Recipient",
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
//                                .weight(1f)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = recipient,
                            fontSize = screenFontSize(x = 14.0).sp,
                            modifier = Modifier
//                                .weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Date",
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
//                                .weight(1f)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = formatIsoDateTime2(LocalDateTime.parse(transactionData.createdAt)),
                            fontSize = screenFontSize(x = 14.0).sp,
                            modifier = Modifier
//                                .weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    if(transactionData.order != null) {
                        HorizontalDivider()
                    }
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Order name",
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
//                                .weight(1f)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = transactionData.order?.name ?: "",
                            fontSize = screenFontSize(x = 14.0).sp,
                            modifier = Modifier
//                                .weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Business",
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
//                                .weight(1f)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = transactionData.order?.business?.name ?: "",
                            fontSize = screenFontSize(x = 14.0).sp,
                            modifier = Modifier
//                                .weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    if(transactionData.order?.courier != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Courier",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
//                                .weight(1f)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = transactionData.order?.courier?.username ?: "",
                                fontSize = screenFontSize(x = 14.0).sp,
                                modifier = Modifier
//                                .weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    }
                }

            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Exit")
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TransactionDetailsScreenPreview() {
    WazipayTheme {
        TransactionDetailsScreen(
            userId = 1,
            transactionData = transactionData,
            navigateToPreviousScreen = {}
        )
    }
}