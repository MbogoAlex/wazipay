package com.escrow.wazipay.ui.screens.users.specific.buyer.dashboard

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.invoice.InvoiceData
import com.escrow.wazipay.data.network.models.invoice.invoices
import com.escrow.wazipay.data.network.models.order.OrderData
import com.escrow.wazipay.data.network.models.order.orders
import com.escrow.wazipay.data.network.models.transaction.TransactionData
import com.escrow.wazipay.data.network.models.transaction.transactions
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.ui.screens.users.common.enums.LoadUserStatus
import com.escrow.wazipay.ui.screens.users.common.invoice.InvoiceItemComposable
import com.escrow.wazipay.ui.screens.users.common.order.OrderItemComposable
import com.escrow.wazipay.ui.screens.users.common.transaction.TransactionCellComposable
import com.escrow.wazipay.ui.screens.users.common.wallet.WalletCard
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.formatMoneyValue
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BuyerDashboardScreenComposable(
    navigateToLoginScreenWithArgs: (phoneNumber: String, pin: String) -> Unit,
    navigateToDepositScreen: () -> Unit,
    navigateToWithdrawalScreen: () -> Unit,
    navigateToBusinessSelectionScreen: () -> Unit,
    navigateToOrderDetailsScreen: (orderId: String, fromPaymentScreen: Boolean) -> Unit,
    navigateToOrderScreen: () -> Unit,
    navigateToInvoicesScreen: () -> Unit,
    navigateToInvoicesScreenWithStatus: (status: String) -> Unit,
    navigateToTransactionsScreen: () -> Unit,
    navigateToInvoiceDetailsScreen: (invoiceId: String) -> Unit,
    navigateToTransactionDetailsScreen: (transactionId: String) -> Unit,
    modifier: Modifier = Modifier
) {

    val viewModel: BuyerDashboardViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        when(lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> {
                viewModel.loadDashboardData()
            }
        }
    }

    if(uiState.unauthorized && uiState.loadUserStatus == LoadUserStatus.FAIL) {
        navigateToLoginScreenWithArgs(uiState.userDetails.phoneNumber!!, uiState.userDetails.pin!!)
        viewModel.resetStatus()
    }

    Box(
        modifier = modifier
            .safeDrawingPadding()
    ) {
        BuyerDashboardScreen(
            walletBalance = formatMoneyValue(uiState.userWalletData.balance),
            userVerified = uiState.userDetailsData.verified,
            username = uiState.userDetails.username ?: "",
            userId = uiState.userDetails.userId,
            orders = uiState.orders,
            invoices = uiState.invoices,
            pendingInvoices = uiState.pendingInvoices,
            transactions = uiState.transactions,
            navigateToDepositScreen = navigateToDepositScreen,
            navigateToWithdrawalScreen = navigateToWithdrawalScreen,
            navigateToBusinessSelectionScreen = navigateToBusinessSelectionScreen,
            navigateToOrderDetailsScreen = navigateToOrderDetailsScreen,
            navigateToOrdersScreen = navigateToOrderScreen,
            navigateToInvoicesScreen = navigateToInvoicesScreen,
            navigateToInvoicesScreenWithStatus = navigateToInvoicesScreenWithStatus,
            navigateToTransactionsScreen = navigateToTransactionsScreen,
            navigateToInvoiceDetailsScreen = navigateToInvoiceDetailsScreen,
            navigateToTransactionDetailsScreen = navigateToTransactionDetailsScreen
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BuyerDashboardScreen(
    walletBalance: String,
    userVerified: Boolean,
    username: String,
    userId: Int,
    orders: List<OrderData>,
    invoices: List<InvoiceData>,
    pendingInvoices: List<InvoiceData>,
    transactions: List<TransactionData>,
    navigateToDepositScreen: () -> Unit,
    navigateToWithdrawalScreen: () -> Unit,
    navigateToBusinessSelectionScreen: () -> Unit,
    navigateToOrderDetailsScreen: (orderId: String, fromPaymentScreen: Boolean) -> Unit,
    navigateToOrdersScreen: () -> Unit,
    navigateToInvoicesScreen: () -> Unit,
    navigateToInvoicesScreenWithStatus: (status: String) -> Unit,
    navigateToTransactionsScreen: () -> Unit,
    navigateToInvoiceDetailsScreen: (invoiceId: String) -> Unit,
    navigateToTransactionDetailsScreen: (transactionId: String) -> Unit,
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
            role = Role.BUYER,
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
        Button(
            onClick = navigateToBusinessSelectionScreen,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pay business",
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Icon(
                    painter = painterResource(id = R.drawable.pay),
                    contentDescription = "Pay business"
                )
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        if(pendingInvoices.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pending invoices",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = screenFontSize(x = 16.0).sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    enabled = pendingInvoices.isNotEmpty(),
                    onClick = {
                    navigateToInvoicesScreenWithStatus("Pending")
                }) {
                    Text(text = "See all")
                }
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
            ) {
                pendingInvoices.take(5).forEach {
                    InvoiceItemComposable(
                        invoiceData = it,
                        dashboardScreen = true,
                        navigateToInvoiceDetailsScreen = navigateToInvoiceDetailsScreen,
                        modifier = Modifier
                            .padding(
                                top = screenHeight(x = 8.0),
                                end = screenWidth(x = 16.0)
                            )
                            .fillMaxWidth(0.7f)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "All invoices",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                enabled = invoices.isNotEmpty(),
                onClick = navigateToInvoicesScreen
            ) {
                Text(text = "See all")
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(invoices.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
            ) {
                pendingInvoices.take(5).forEach {
                    InvoiceItemComposable(
                        invoiceData = it,
                        dashboardScreen = true,
                        navigateToInvoiceDetailsScreen = navigateToInvoiceDetailsScreen,
                        modifier = Modifier
                            .padding(
                                top = screenHeight(x = 8.0),
                                end = screenWidth(x = 16.0)
                            )
                            .fillMaxWidth(0.7f)
                    )
                }
            }
        } else {
            Text(
                text = "No payments (invoices) found",
                fontSize = screenFontSize(x = 14.0).sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Orders",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                enabled = orders.isNotEmpty(),
                onClick = navigateToOrdersScreen
            ) {
                Text(text = "See all")
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        if(orders.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
            ) {
                orders.take(5).forEach {
                    OrderItemComposable(
                        homeScreen = true,
                        orderData = it,
                        navigateToOrderDetailsScreen = navigateToOrderDetailsScreen,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(
                                end = screenWidth(x = 16.0)
                            )
                    )
                }
            }
        } else {
            Text(
                text = "No orders found",
                fontSize = screenFontSize(x = 14.0).sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
//        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Buyer Transactions",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                enabled = transactions.isNotEmpty(),
                onClick = navigateToTransactionsScreen
            ) {
                Text(
                    text = "See all",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(transactions.isNotEmpty()) {
            transactions.take(5).forEach {
                TransactionCellComposable(
                    userId = userId,
                    role = Role.BUYER,
                    transactionData = it,
                    navigateToTransactionDetailsScreen = navigateToTransactionDetailsScreen,
                    modifier = Modifier
                        .padding(
//                            top = screenHeight(x = 8.0)
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
fun BuyerDashboardScreenPreview() {
    WazipayTheme {
        BuyerDashboardScreen(
            walletBalance = "Ksh1,000",
            userVerified = true,
            username = "Alex Mbogo",
            userId = 1,
            orders = orders,
            invoices = invoices,
            pendingInvoices = invoices,
            transactions = transactions,
            navigateToDepositScreen = {},
            navigateToWithdrawalScreen = {},
            navigateToBusinessSelectionScreen = {},
            navigateToOrderDetailsScreen = {orderId, fromPaymentScreen ->  },
            navigateToOrdersScreen = {},
            navigateToInvoicesScreen = {},
            navigateToInvoicesScreenWithStatus = {},
            navigateToTransactionsScreen = {},
            navigateToInvoiceDetailsScreen = {},
            navigateToTransactionDetailsScreen = {}
        )
    }
}