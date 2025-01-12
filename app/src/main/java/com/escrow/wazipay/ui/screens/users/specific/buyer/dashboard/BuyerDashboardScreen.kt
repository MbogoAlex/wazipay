package com.escrow.wazipay.ui.screens.users.specific.buyer.dashboard

import android.os.Build
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
import com.escrow.wazipay.ui.screens.users.common.enums.LoadUserStatus
import com.escrow.wazipay.ui.screens.users.common.invoice.InvoiceItemComposable
import com.escrow.wazipay.ui.screens.users.common.order.OrderItemComposable
import com.escrow.wazipay.ui.screens.users.common.transaction.TransactionCellComposable
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
            navigateToInvoiceDetailsScreen = navigateToInvoiceDetailsScreen
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
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(screenWidth(x = 16.0))
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.person),
                        contentDescription = null
                    )
                    Text(
                        text = username.split(" ")[0].uppercase(),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    ElevatedCard {
                        Text(
                            text = if(userVerified) "VERIFIED" else "UNVERIFIED",
                            fontSize = screenFontSize(x = 12.0).sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .padding(screenWidth(x = 3.0))
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "BUYER",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Text(
                    text = "Wallet Balance",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                if(!walletExpanded) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .clickable {
                                walletExpanded = !walletExpanded
                            }
                            .fillMaxWidth()
                    ) {
                        Text(text = "Click to expand")
                        Icon(
                            painter = painterResource(id = R.drawable.double_arrow_right),
                            contentDescription = "Expand wallet"
                        )
                    }
                }
                if(walletExpanded) {
                    Column {
                        Text(
                            text = walletBalance,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 24.0).sp
                        )
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(onClick = {
                                navigateToDepositScreen()
                            }) {
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
                            OutlinedButton(onClick = {
                                navigateToWithdrawalScreen()
                            }) {
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
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Switch(checked = false, onCheckedChange = {})
                        }
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                        if(walletExpanded) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .clickable {
                                        walletExpanded = !walletExpanded
                                    }
                                    .fillMaxWidth()
                            ) {
                                Text(text = "Click to collapse")
                                Icon(
                                    painter = painterResource(id = R.drawable.double_arrow_left),
                                    contentDescription = "Collapse wallet"
                                )
                            }
                        }
                    }
                }

            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
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
            pendingInvoices.take(5).forEach {
                InvoiceItemComposable(
                    invoiceData = it,
                    navigateToInvoiceDetailsScreen = navigateToInvoiceDetailsScreen,
                    modifier = Modifier
                        .padding(
                            top = screenHeight(x = 8.0)
                        )
                )
            }
        }
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
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
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
                                screenWidth(x = 8.0)
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
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Payments (invoices)",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = navigateToInvoicesScreen) {
                Text(text = "See all")
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(invoices.isNotEmpty()) {
            invoices.take(5).forEach {
                InvoiceItemComposable(
                    invoiceData = it,
                    navigateToInvoiceDetailsScreen = navigateToInvoiceDetailsScreen,
                    modifier = Modifier
                        .padding(
                            top = screenHeight(x = 8.0)
                        )
                )
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
        Spacer(modifier = Modifier.height(screenHeight(x = 24.0)))
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
            navigateToInvoiceDetailsScreen = {}
        )
    }
}