package com.escrow.wazipay.ui.screens.users.specific.merchant.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.business.businesses
import com.escrow.wazipay.data.network.models.invoice.InvoiceData
import com.escrow.wazipay.data.network.models.order.OrderData
import com.escrow.wazipay.data.network.models.order.orders
import com.escrow.wazipay.data.network.models.transaction.TransactionData
import com.escrow.wazipay.ui.screens.users.common.business.BusinessCellComposable
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
fun MerchantDashboardScreenComposable(
    navigateToDepositScreen: () -> Unit,
    navigateToWithdrawalScreen: () -> Unit,
    navigateToLoginScreenWithArgs: (phoneNumber: String, pin: String) -> Unit,
    navigateToOrderDetailsScreen: (orderId: String, fromPaymentScreen: Boolean) -> Unit,
    navigateToBusinessDetailsScreen: (userId: String) -> Unit,
    navigateToInvoiceDetailsScreen: (invoiceId: String) -> Unit,
    navigateToOrdersScreen: () -> Unit,
    navigateToOrdersScreenWithStatus: (status: String) -> Unit,
    navigateToInvoicesScreen: () -> Unit,
    navigateToBusinessSelectionScreen: () -> Unit,
    navigateToBusinessSelectionScreenWithArgs: (toBuyerSelectionScreen: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val merchantDashboardViewModel: MerchantDashboardViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val merchantDashboardUiState by merchantDashboardViewModel.uiState.collectAsState()

    if(merchantDashboardUiState.unauthorized && merchantDashboardUiState.loadUserStatus == LoadUserStatus.FAIL) {
        navigateToLoginScreenWithArgs(merchantDashboardUiState.userDetails.phoneNumber!!, merchantDashboardUiState.userDetails.pin!!)
        merchantDashboardViewModel.resetStatus()
    }

    Box(
        modifier = modifier
            .safeDrawingPadding()
    ) {
        MerchantDashboardScreen(
            userId = merchantDashboardUiState.userDetails.userId,
            username = merchantDashboardUiState.userDetails.username ?: "",
            walletBalance = formatMoneyValue(merchantDashboardUiState.userWalletData.balance),
            userVerified = merchantDashboardUiState.userDetailsData.verified,
            orders = merchantDashboardUiState.orders,
            pendingOrders = merchantDashboardUiState.pendingOrders,
            invoices = merchantDashboardUiState.invoices,
            businesses = merchantDashboardUiState.businesses,
            transactions = merchantDashboardUiState.transactions,
            navigateToDepositScreen = navigateToDepositScreen,
            navigateToWithdrawalScreen = navigateToWithdrawalScreen,
            navigateToOrderDetailsScreen = navigateToOrderDetailsScreen,
            navigateToBusinessDetailsScreen = navigateToBusinessDetailsScreen,
            navigateToInvoiceDetailsScreen = navigateToInvoiceDetailsScreen,
            navigateToOrdersScreen = navigateToOrdersScreen,
            navigateToOrdersScreenWithStatus = navigateToOrdersScreenWithStatus,
            navigateToInvoicesScreen = navigateToInvoicesScreen,
            navigateToBusinessSelectionScreen = navigateToBusinessSelectionScreen,
            navigateToBusinessSelectionScreenWithArgs = navigateToBusinessSelectionScreenWithArgs
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MerchantDashboardScreen(
    userId: Int,
    username: String,
    walletBalance: String,
    userVerified: Boolean,
    orders: List<OrderData>,
    pendingOrders: List<OrderData>,
    invoices: List<InvoiceData>,
    businesses: List<BusinessData>,
    transactions: List<TransactionData>,
    navigateToDepositScreen: () -> Unit,
    navigateToWithdrawalScreen: () -> Unit,
    navigateToOrderDetailsScreen: (orderId: String, fromPaymentScreen: Boolean) -> Unit,
    navigateToBusinessDetailsScreen: (userId: String) -> Unit,
    navigateToInvoiceDetailsScreen: (invoiceId: String) -> Unit,
    navigateToOrdersScreen: () -> Unit,
    navigateToOrdersScreenWithStatus: (status: String) -> Unit,
    navigateToInvoicesScreen: () -> Unit,
    navigateToBusinessSelectionScreen: () -> Unit,
    navigateToBusinessSelectionScreenWithArgs: (toBuyerSelectionScreen: Boolean) -> Unit,
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
                        text = "MERCHANT",
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
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .padding(screenWidth(x = 8.0))
                    .border(
                        width = screenWidth(x = 1.0),
                        color = Color.LightGray,
                        shape = RoundedCornerShape(screenWidth(x = 10.0))
                    )
                    .fillMaxWidth(0.4f)
                    .clickable { }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(screenWidth(x = 16.0))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.add),
                        contentDescription = "Add business"
                    )
                    Text(text = "Add business")
                }
            }
            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
            Box(
                modifier = Modifier
                    .padding(screenWidth(x = 8.0))
                    .border(
                        width = screenWidth(x = 1.0),
                        color = Color.LightGray,
                        shape = RoundedCornerShape(screenWidth(x = 10.0))
                    )
                    .fillMaxWidth(0.4f)
                    .clickable {
                        navigateToBusinessSelectionScreenWithArgs(true)
                    }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(screenWidth(x = 16.0))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.issue_invoice),
                        contentDescription = "Issue invoice"
                    )
                    Text(text = "Issue invoice")
                }
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Orders pending pickup",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                enabled = pendingOrders.isNotEmpty(),
                onClick = { navigateToOrdersScreenWithStatus("pending_pickup") }
            ) {
                Text(
                    text = "See all",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
        if(pendingOrders.isNotEmpty()) {
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
            ) {
                pendingOrders.take(5).forEach {
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
                text = "No new order",
                fontSize = screenFontSize(x = 14.0).sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Button(
            enabled = pendingOrders.isNotEmpty(),
            onClick = {
                navigateToOrdersScreenWithStatus("pending")
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Courier assignment",
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Icon(
                    painter = painterResource(id = R.drawable.motorbike),
                    contentDescription = "Courier assignment"
                )
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent orders",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                enabled = orders.isNotEmpty(),
                onClick = navigateToOrdersScreen
            ) {
                Text(
                    text = "See all",
                    fontSize = screenFontSize(x = 14.0).sp
                )
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
                text = "Issued invoices",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                enabled = invoices.isNotEmpty(),
                onClick = navigateToInvoicesScreen
            ) {
                Text(
                    text = "See all",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
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
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Button(
                onClick = {
                    navigateToBusinessSelectionScreenWithArgs(true)
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Issue an invoice",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Icon(
                        painter = painterResource(id = R.drawable.issue_invoice),
                        contentDescription = "Courier assignment"
                    )
                }
            }
        } else {
            Text(
                text = "No invoices found",
                fontSize = screenFontSize(x = 14.0).sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            OutlinedButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.issue_invoice), 
                        contentDescription = "Issue an invoice"
                    )
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    Text(
                        text = "Issue your first invoice",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My businesses",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                enabled = businesses.isNotEmpty(),
                onClick = { /*TODO*/ }
            ) {
                Text(
                    text = "See all",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        if(businesses.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
            ) {
                businesses.take(5).forEach {
                    BusinessCellComposable(
                        homeScreen = true,
                        userId = userId,
                        businessData = it,
                        navigateToBusinessDetailsScreen = navigateToBusinessDetailsScreen,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(
                                screenWidth(x = 8.0)
                            )
                    )
                }
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Add a business",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Icon(
                        painter = painterResource(id = R.drawable.add),
                        contentDescription = "Add a business"
                    )
                }
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My businesses",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = screenFontSize(x = 16.0).sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = { /*TODO*/ }) {
                    Text(
                        text = "See all",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            OutlinedButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add first business"
                    )
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    Text(
                        text = "Add your first business",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
            }
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
                onClick = { /*TODO*/ }
            ) {
                Text(
                    text = "See all",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
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
fun MerchantDashboardScreenPreview() {
    WazipayTheme {
        MerchantDashboardScreen(
            userId = 1,
            username = "Alex Mboo",
            walletBalance = formatMoneyValue(1500.0),
            orders = emptyList(),
            pendingOrders = emptyList(),
            businesses = businesses,
            invoices = emptyList(),
            transactions = emptyList(),
            userVerified = true,
            navigateToDepositScreen = {},
            navigateToWithdrawalScreen = {},
            navigateToOrderDetailsScreen = {orderId, fromPaymentScreen ->  },
            navigateToBusinessDetailsScreen = {},
            navigateToInvoiceDetailsScreen = {},
            navigateToOrdersScreen = {},
            navigateToOrdersScreenWithStatus = {},
            navigateToBusinessSelectionScreen = {},
            navigateToInvoicesScreen = {},
            navigateToBusinessSelectionScreenWithArgs = {}
        )
    }
}