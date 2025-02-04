package com.escrow.wazipay.ui.screens.users.common.transaction

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.escrow.wazipay.data.network.models.transaction.TransactionData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.utils.formatIsoDateTime
import com.escrow.wazipay.utils.formatMoneyValue
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenWidth
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionCellComposable(
    userId: Int,
    role: Role?,
    transactionData: TransactionData,
    navigateToTransactionDetailsScreen: (transactionId: String) -> Unit,
    modifier: Modifier = Modifier
) {

    val transactionType = when(transactionData.transactionType) {
        "WALLET_DEPOSIT" -> "Deposit"
        "WALLET_WITHDRAWAL" -> "Withdrawal"
        "ESCROW_PAYMENT" -> "Payment"
        "ESCROW_RELEASE" -> "Release"
        "ESCROW_REFUND_TO_BUYER" -> "Refund"
        "MERCHANT_REFUND_TO_BUYER" -> "Refund"
        "COURIER_PAYMENT" -> "Courier payment"
        else -> "N/A"
    }.uppercase()
"- ${transactionData.amount}"
    val amount = when(role) {
        Role.BUYER -> {
            when(transactionData.transactionType) {
                "WALLET_DEPOSIT" -> "+ ${transactionData.amount}"
                "WALLET_WITHDRAWAL" -> "- ${transactionData.amount}"
                "ESCROW_PAYMENT" -> "- ${transactionData.amount}"
                "ESCROW_RELEASE" -> "+ ${transactionData.amount}"
                "ESCROW_REFUND_TO_BUYER" -> "+ ${transactionData.amount}"
                "MERCHANT_REFUND_TO_BUYER" -> "+ ${transactionData.amount}"
                "COURIER_PAYMENT" -> "+ ${transactionData.amount}"
                else -> "N/A"
            }
        }
        Role.MERCHANT -> {
            when(transactionData.transactionType) {
                "WALLET_DEPOSIT" -> "+ ${transactionData.amount}"
                "WALLET_WITHDRAWAL" -> "- ${transactionData.amount}"
                "ESCROW_PAYMENT" -> "- ${transactionData.amount}"
                "ESCROW_RELEASE" -> "+ ${transactionData.amount}"
                "ESCROW_REFUND_TO_BUYER" -> "- ${transactionData.amount}"
                "MERCHANT_REFUND_TO_BUYER" -> "- ${transactionData.amount}"
                "COURIER_PAYMENT" -> "- ${transactionData.amount}"
                else -> "N/A"
            }
        }
        Role.COURIER -> {
            when(transactionData.transactionType) {
                "WALLET_DEPOSIT" -> "+ ${transactionData.amount}"
                "WALLET_WITHDRAWAL" -> "- ${transactionData.amount}"
                "ESCROW_PAYMENT" -> "- ${transactionData.amount}"
                "ESCROW_RELEASE" -> "+ ${transactionData.amount}"
                "ESCROW_REFUND_TO_BUYER" -> "- ${transactionData.amount}"
                "MERCHANT_REFUND_TO_BUYER" -> "- ${transactionData.amount}"
                "COURIER_PAYMENT" -> "+ ${transactionData.amount}"
                else -> "N/A"
            }
        }

        null -> {
            when(transactionData.transactionType) {
                "WALLET_DEPOSIT" -> "+ ${transactionData.amount}"
                "WALLET_WITHDRAWAL" -> "- ${transactionData.amount}"
                "ESCROW_PAYMENT" -> {
                    if(transactionData.order!!.merchant!!.id == userId) {
                        "+ ${transactionData.amount}"
                    } else {
                        "- ${transactionData.amount}"
                    }
                }
                "ESCROW_RELEASE" -> "+ ${transactionData.amount}"
                "ESCROW_REFUND_TO_BUYER" -> "+ ${transactionData.amount}"
                "MERCHANT_REFUND_TO_BUYER" -> {
                    if(transactionData.order!!.merchant!!.id == userId) {
                        "- ${transactionData.amount}"
                    } else {
                        "+ ${transactionData.amount}"
                    }
                }
                "COURIER_PAYMENT" -> {
                    if(transactionData.order!!.courier!!.id == userId) {
                        "+ ${transactionData.amount}"
                    } else {
                        "- ${transactionData.amount}"
                    }
                }
                else -> "N/A"
            }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
//            .padding(screenWidth(x = 8.0))
            .clickable {
                navigateToTransactionDetailsScreen(transactionData.id.toString())
            }
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(screenWidth(x = 8.0))
        ) {
            Text(
                text = if(transactionData.order != null)  if(transactionData.order.name.length > 25) transactionData.order.name.take(25) + "..." else transactionData.order.name else transactionType,
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 14.0).sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = transactionType,
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 16.0).sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = if(transactionData.createdAt != null) formatIsoDateTime(LocalDateTime.parse(transactionData.createdAt)) else "TIME: N/A",
                fontWeight = FontWeight.W300,
                fontSize = screenFontSize(x = 12.0).sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = amount,
                color = if(amount.startsWith("-")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 16.0).sp
            )
            Text(
                text = "KES",
                fontWeight = FontWeight.W300,
                fontSize = screenFontSize(x = 12.0).sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}