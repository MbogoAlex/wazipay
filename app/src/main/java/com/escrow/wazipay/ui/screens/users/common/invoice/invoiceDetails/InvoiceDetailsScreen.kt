package com.escrow.wazipay.ui.screens.users.common.invoice.invoiceDetails

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.invoice.InvoiceData
import com.escrow.wazipay.data.network.models.invoice.invoiceData
import com.escrow.wazipay.data.network.models.order.OrderData
import com.escrow.wazipay.data.network.models.order.orderData
import com.escrow.wazipay.data.network.models.user.UserContactData
import com.escrow.wazipay.data.network.models.user.userContactData
import com.escrow.wazipay.data.network.models.wallet.UserWalletData
import com.escrow.wazipay.data.network.models.wallet.userWalletData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.screens.users.common.invoice.LoadInvoicesStatus
import com.escrow.wazipay.ui.screens.users.common.order.OrderItemComposable
import com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment.PaymentMethod
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.LoadingStatus
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.composables.TextFieldComposable
import com.escrow.wazipay.utils.formatMoneyValue
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

object InvoiceDetailsScreenDestination: AppNavigation {
    override val title: String = "Invoice details screen"
    override val route: String = "invoice-details-screen"
    val invoiceId: String = "invoiceId"
    val routeWithInvoiceId: String = "$route/{$invoiceId}"

}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InvoiceDetailsScreenComposable(
    navigateToOrderDetailsScreen: (orderId: String, fromPaymentScreen: Boolean) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val viewModel: InvoiceDetailsViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.loadInvoicesStatus == LoadInvoicesStatus.LOADING,
        onRefresh = {
            viewModel.getInvoiceScreenData()

        }
    )

    var showConfirmDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var showSuccessDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if(uiState.loadingStatus == LoadingStatus.SUCCESS) {
        showSuccessDialog = true
    }

    if(showConfirmDialog) {
        InvoicePaymentConfirmationDialog(
            onConfirm = {
                showConfirmDialog = !showConfirmDialog
                if(uiState.invoiceData.amount > uiState.userWalletData.balance) {
                    Toast.makeText(context, "Insufficient funds", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.payInvoice()
                }
            },
            onDismiss = {
                showConfirmDialog = !showConfirmDialog
            },
            title = uiState.invoiceData.title,
            cost = formatMoneyValue(uiState.invoiceData.amount)
        )
    }

    if(showSuccessDialog) {
        InvoicePaymentSuccessDialog(
            onConfirm = {
                viewModel.resetStatus()
                viewModel.getInvoiceDetails()
                viewModel.getUserWallet()
                showSuccessDialog = !showSuccessDialog
                navigateToOrderDetailsScreen(uiState.newOrderId.toString(), true)
            },
            onDismiss = {
                viewModel.resetStatus()
                viewModel.getInvoiceDetails()
                viewModel.getUserWallet()
                showSuccessDialog = !showSuccessDialog
                navigateToOrderDetailsScreen(uiState.newOrderId.toString(), true)
            },
            title = uiState.invoiceData.title,
            cost = formatMoneyValue(uiState.invoiceData.amount)
        )
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        InvoiceDetailsScreen(
            pullRefreshState = pullRefreshState,
            invoiceData = uiState.invoiceData,
            role = uiState.role,
            userWalletData = uiState.userWalletData,
            orderData = uiState.orderData,
            buyer = uiState.invoiceData.buyer,
            merchant = uiState.invoiceData.merchant,
            phoneNumber = uiState.phoneNumber,
            onChangePhoneNumber = {
                viewModel.changePhoneNumber(it)
                viewModel.enableButton()
            },
            paymentMethod = uiState.paymentMethod,
            onChangePaymentMethod = {
                viewModel.changePaymentMethod(it)
                viewModel.enableButton()
            },
            navigateToOrderDetailsScreen = navigateToOrderDetailsScreen,
            navigateToPreviousScreen = navigateToPreviousScreen,
            loadingStatus = uiState.loadingStatus,
            loadInvoicesStatus = uiState.loadInvoicesStatus,
            onPayInvoice = {
                showConfirmDialog = !showConfirmDialog
            },
            buttonEnabled = uiState.buttonEnabled
        )
    }


}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InvoiceDetailsScreen(
    pullRefreshState: PullRefreshState?,
    invoiceData: InvoiceData,
    role: Role,
    userWalletData: UserWalletData,
    orderData: OrderData?,
    buyer: UserContactData,
    merchant: UserContactData,
    phoneNumber: String,
    onChangePhoneNumber: (phone: String) -> Unit,
    paymentMethod: PaymentMethod,
    onChangePaymentMethod: (paymentMethod: PaymentMethod) -> Unit,
    navigateToOrderDetailsScreen: (orderId: String, fromPaymentScreen: Boolean) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    loadingStatus: LoadingStatus,
    loadInvoicesStatus: LoadInvoicesStatus,
    onPayInvoice: () -> Unit,
    buttonEnabled: Boolean,
    modifier: Modifier = Modifier
) {



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = screenHeight(x = 16.0),
                horizontal = screenWidth(x = 16.0)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = navigateToPreviousScreen) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Text(
                text = if(role == Role.BUYER) "Payment (invoice) details" else "Issued Invoice details",
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 14.0).sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(loadInvoicesStatus == LoadInvoicesStatus.LOADING) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                PullRefreshIndicator(
                    refreshing = true,
                    state = pullRefreshState!!
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .pullRefresh(pullRefreshState!!)
            ) {
                InvoiceDetailsCard(
                    role = role,
                    invoiceData = invoiceData,
                    userWalletData = userWalletData,
                    paymentMethod = paymentMethod,
                    onChangePaymentMethod = onChangePaymentMethod,
                    phoneNumber = phoneNumber,
                    onChangePhoneNumber = onChangePhoneNumber,
                    loadingStatus = loadingStatus,
                    buttonEnabled = buttonEnabled,
                    onPayInvoice = onPayInvoice
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Text(
                    text = "Issued to:",
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                ActorCard(
                    role = Role.BUYER,
                    userContactData = buyer
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Text(
                    text = "Issued by:",
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                ActorCard(
                    role = Role.MERCHANT,
                    userContactData = merchant
                )
                if(invoiceData.orderId != null) {
                    Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                    Text(
                        text = "Order details",
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    OrderItemComposable(
                        homeScreen = false,
                        orderData = orderData!!,
                        navigateToOrderDetailsScreen = navigateToOrderDetailsScreen
                    )
                }
            }
        }

    }


}

@Composable
fun InvoiceDetailsCard(
    role: Role,
    invoiceData: InvoiceData,
    userWalletData: UserWalletData,
    paymentMethod: PaymentMethod,
    onChangePaymentMethod: (paymentMethod: PaymentMethod) -> Unit,
    phoneNumber: String,
    onChangePhoneNumber: (phone: String) -> Unit,
    loadingStatus: LoadingStatus,
    onPayInvoice: () -> Unit,
    buttonEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = screenWidth(x = 1.0),
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
            .padding(
                screenWidth(x = 16.0)
            )
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status",
                    fontWeight = FontWeight.Bold,
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Text(
                    text = invoiceData.invoiceStatus,
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Title",
                    fontWeight = FontWeight.Bold,
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Text(
                    text = invoiceData.title,
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Amount to pay",
                    fontWeight = FontWeight.Bold,
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Text(
                    text = formatMoneyValue(invoiceData.amount),
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = invoiceData.invoiceStatus,
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
            if(invoiceData.invoiceStatus == "PENDING" && role == Role.BUYER) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = screenWidth(x = 16.0))
                ) {
                    RadioButton(
                        selected = paymentMethod == PaymentMethod.WAZIPAY,
                        onClick = { onChangePaymentMethod(PaymentMethod.WAZIPAY) }
                    )
                    Text(
                        text = "Wazipay",
                        modifier = Modifier.padding(start = screenWidth(x = 8.0)),
                        fontSize = screenFontSize(x = 14.0).sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.width(screenWidth(x = 16.0)))

                    RadioButton(
                        selected = paymentMethod == PaymentMethod.MPESA,
                        onClick = { onChangePaymentMethod(PaymentMethod.MPESA) }
                    )
                    Text(
                        text = "M-PESA",
                        modifier = Modifier.padding(start = screenWidth(x = 8.0)),
                        fontSize = screenFontSize(x = 14.0).sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                if(paymentMethod == PaymentMethod.WAZIPAY) {
                    ElevatedCard(
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
                                Image(
                                    painter = painterResource(id = R.drawable.wazipay_logo),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(screenWidth(x = 24.0))
                                )
                                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                                Text(
                                    text = "Wazipay wallet",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Current balance: ")
                                Text(text = formatMoneyValue(userWalletData.balance))
                            }
                            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Pay: ")
                                Text(text = formatMoneyValue(invoiceData.amount))
                            }

                        }
                    }
                } else if(paymentMethod == PaymentMethod.MPESA) {
                    ElevatedCard(
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
                                Image(
                                    painter = painterResource(id = R.drawable.mpesa_logo),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(screenWidth(x = 24.0))
                                )
                                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                                Text(
                                    text = "M-PESA",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                            TextFieldComposable(
                                shape = RoundedCornerShape(screenWidth(x = 10.0)),
                                label = "Mpesa phone number",
                                value = phoneNumber,
                                leadingIcon = R.drawable.phone,
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Number
                                ),
                                onValueChange = onChangePhoneNumber,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )

                        }
                    }
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Button(
                    enabled = buttonEnabled && loadingStatus != LoadingStatus.LOADING,
                    onClick = onPayInvoice,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "Pay ${formatMoneyValue(invoiceData.amount)}")
                }
            }
        }
    }
}

@Composable
fun ActorCard(
    role: Role,
    userContactData: UserContactData,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = screenWidth(x = 1.0),
                color = Color.LightGray,
                shape = RoundedCornerShape(screenWidth(x = 10.0))
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(screenWidth(x = 16.0))
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(screenWidth(x = 16.0))
            ) {
                Icon(
                    painter = painterResource(id = if(role == Role.BUYER) R.drawable.buyer else R.drawable.seller),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
            Column {
                Text(text = userContactData.username)
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.phone),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = userContactData.phoneNumber,
                        fontSize = screenFontSize(x = 14.0).sp,
//                            fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.email),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = userContactData.email,
                        fontSize = screenFontSize(x = 14.0).sp,
                    )
                }
            }
        }
    }
}

@Composable
fun InvoicePaymentConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String,
    cost: String,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Confirm Invoice Payment",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Are you sure you want to pay $cost for $title?",
                fontSize = screenFontSize(x = 14.0).sp
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(
                    text = "Confirm",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    )
}

@Composable
fun InvoicePaymentSuccessDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String,
    cost: String,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Invoice Payment and Order Creation",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Your invoice payment has been done and an order has been created for $title",
                fontSize = screenFontSize(x = 14.0).sp
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(
                    text = "Done",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InvoiceDetailsScreenPreview(
    modifier: Modifier = Modifier
) {
    WazipayTheme {
        InvoiceDetailsScreen(
            pullRefreshState = null,
            invoiceData = invoiceData,
            role = Role.BUYER,
            paymentMethod = PaymentMethod.WAZIPAY,
            buyer = userContactData,
            merchant = userContactData,
            phoneNumber = "0794649026",
            onChangePaymentMethod = {},
            userWalletData = userWalletData,
            onChangePhoneNumber = {},
            navigateToPreviousScreen = {},
            orderData = orderData,
            navigateToOrderDetailsScreen = {orderId: String, fromPaymentScreen: Boolean ->},
            loadingStatus = LoadingStatus.INITIAL,
            loadInvoicesStatus = LoadInvoicesStatus.INITIAL,
            onPayInvoice = {},
            buttonEnabled = false
        )
    }
}