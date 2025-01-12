package com.escrow.wazipay.ui.screens.users.specific.courier

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.order.OrderData
import com.escrow.wazipay.data.network.models.order.orders
import com.escrow.wazipay.data.network.models.transaction.TransactionData
import com.escrow.wazipay.data.network.models.transaction.transactions
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.ui.screens.users.common.order.OrderItemComposable
import com.escrow.wazipay.ui.screens.users.common.transaction.TransactionCellComposable
import com.escrow.wazipay.ui.screens.users.common.wallet.WalletCard
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.formatMoneyValue
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

@Composable
fun CourierScreenDashboardScreenComposable(
    modifier: Modifier = Modifier,
) {

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CourierScreenDashboardScreen(
    username: String,
    userVerified: Boolean,
    walletBalance: String,
    assignedOrders: List<OrderData>,
    transactions: List<TransactionData>,
    navigateToDepositScreen: () -> Unit,
    navigateToWithdrawalScreen: () -> Unit,
    navigateToOrdersScreenWithStatus: (childScreen: String) -> Unit,
    navigateToOrderDetailsScreen: (orderId: String, fromPaymentScreen: Boolean) -> Unit,
    navigateToTransactionsScreen: () -> Unit,
    modifier: Modifier = Modifier
) {

    var walletExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = screenWidth(x = 16.0),
                end = screenWidth(x = 16.0),
                bottom = screenHeight(x = 16.0)
            )
            .verticalScroll(rememberScrollState())
    ) {
        WalletCard(
            walletExpanded = walletExpanded,
            role = Role.COURIER,
            username = username,
            userVerified = userVerified,
            walletBalance = walletBalance,
            navigateToDepositScreen = navigateToDepositScreen,
            navigateToWithdrawalScreen = navigateToWithdrawalScreen,
            onExpandWallet = {
                walletExpanded = !walletExpanded
            }
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Assigned orders pending delivery",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                enabled = assignedOrders.isNotEmpty(),
                onClick = { navigateToOrdersScreenWithStatus("in_transit") },
            ) {
                Text(
                    text = "See all",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
        if(assignedOrders.isNotEmpty()) {
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
            ) {
                assignedOrders.take(5).forEach {
                    OrderItemComposable(
                        homeScreen = true,
                        orderData = it,
                        navigateToOrderDetailsScreen = navigateToOrderDetailsScreen,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(
                                screenWidth(x = 8.0)
                            )
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            Text(
                text = "No pending assigned order",
                fontSize = screenFontSize(x = 14.0).sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                        .align(Alignment.CenterHorizontally)
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Button(
            onClick = { navigateToOrdersScreenWithStatus("in_transit") },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row {
                Text(
                    text = "Complete delivery",
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Icon(
                    painter = painterResource(id = R.drawable.motorbike),
                    contentDescription = "Complete delivery"
                )
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Transactions",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                enabled = transactions.isNotEmpty(),
                onClick = navigateToTransactionsScreen
            ) {
                Text(text = "See all")
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(transactions.isNotEmpty()) {
            transactions.take(5).forEach {
                TransactionCellComposable(
                    transactionData = it,
                    modifier = Modifier
                        .padding(
                            top = screenHeight(x = 8.0)
                        )
                )
            }
        } else {
            Text(
                text = "No transactions found",
                fontSize = screenFontSize(x = 14.0).sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CourierScreenDashboardScreenPreview() {
    WazipayTheme {
        CourierScreenDashboardScreen(
            username = "Alex Mbogo",
            userVerified = true,
            assignedOrders = orders,
            transactions = transactions,
            walletBalance = formatMoneyValue(1560.0),
            navigateToDepositScreen = {},
            navigateToWithdrawalScreen = {},
            navigateToOrdersScreenWithStatus = {},
            navigateToOrderDetailsScreen = {_, _ ->},
            navigateToTransactionsScreen = {}
        )
    }
}