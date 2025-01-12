package com.escrow.wazipay.ui.screens.users.common.order.orderDetails

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.order.OrderData
import com.escrow.wazipay.data.network.models.order.orderData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.screens.users.common.order.CompleteDeliveryStatus
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.formatIsoDateTime
import com.escrow.wazipay.utils.formatMoneyValue
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth
import java.time.LocalDateTime

object OrderDetailsScreenDestination: AppNavigation {
    override val title: String = "Order details screen"
    override val route: String = "order-details-screen"
    val orderId: String = "orderId"
    val fromPaymentScreen: String = "fromPaymentScreen"
    val routeWithArgs: String = "$route/{$orderId}/{$fromPaymentScreen}"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderDetailsScreenComposable(
    navigateToPreviousScreen: () -> Unit,
    navigateToDashboardScreen: () -> Unit,
    navigateToCourierSelectionScreen: (orderId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: OrderDetailsViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    var showConfirmationDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var showSuccessDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if(uiState.completeDeliveryStatus == CompleteDeliveryStatus.SUCCESS) {
        showSuccessDialog = true
    }

    BackHandler(onBack = {
        if(uiState.fromPaymentScreen) {
            navigateToDashboardScreen()
        } else {
            navigateToPreviousScreen()
        }
    })

    if(showConfirmationDialog) {
        CompleteDeliveryConfirmationDialog(
            onConfirm = {
                showConfirmationDialog = !showConfirmationDialog
                viewModel.completeDelivery()
            },
            onDismiss = {
                showConfirmationDialog = !showConfirmationDialog
            },
            orderName = uiState.orderData.name,
            buyerName = uiState.orderData.buyer?.username ?: ""
        )
    }

    if(showSuccessDialog) {
        CompleteDeliverySuccessDialog(
            onConfirm = {
                viewModel.getOrder()
                showSuccessDialog = !showSuccessDialog
            },
            onDismiss = {
                showSuccessDialog = !showSuccessDialog
            },
            orderName = uiState.orderData.name,
            buyerName = uiState.orderData.buyer?.username ?: ""
        )
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        OrderDetailsScreen(
            fromPaymentScreen = uiState.fromPaymentScreen,
            userId = uiState.userDetails.userId,
            orderData = uiState.orderData,
            role = uiState.role,
            navigateToPreviousScreen = navigateToPreviousScreen,
            navigateToDashboardScreen = navigateToDashboardScreen,
            navigateToCourierSelectionScreen = navigateToCourierSelectionScreen,
            onCompleteDelivery = {
                showConfirmationDialog = !showConfirmationDialog
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderDetailsScreen(
    fromPaymentScreen: Boolean,
    userId: Int,
    role: Role,
    orderData: OrderData,
    navigateToPreviousScreen: () -> Unit,
    navigateToDashboardScreen: () -> Unit,
    navigateToCourierSelectionScreen: (orderId: String) -> Unit,
    onCompleteDelivery: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                vertical = screenHeight(x = 16.0),
                horizontal = screenWidth(x = 16.0)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
//                enabled = invoiceCreationStatus != InvoiceCreationStatus.LOADING,
                onClick = {
                    if(fromPaymentScreen) {
                        navigateToDashboardScreen()
                    } else {
                        navigateToPreviousScreen()
                    }
                }
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.onBackground,
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Text(
                text = "Order details",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 16.0).sp
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = orderData.orderStage == "PENDING_PICKUP", onClick = { /*TODO*/ })
            Text(
                text = "Pending pickup",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = screenFontSize(x = 14.0).sp,
            )
        }
        VerticalDivider(
            modifier = Modifier
                .height(screenHeight(x = 32.0))
                .padding(
                    horizontal = screenWidth(x = 22.0)
                )
        )
//        Spacer(modifier = Modifier.height(screenHeight(x = 32.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = orderData.orderStage == "IN_TRANSIT", onClick = { /*TODO*/ })
            Text(
                text = "In transit",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = screenFontSize(x = 14.0).sp,
            )
            if(role == Role.MERCHANT && orderData.orderStage == "PENDING_PICKUP") {
                Button(
                    onClick = { navigateToCourierSelectionScreen(orderData.id.toString()) },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Assign courier",
                            fontSize = screenFontSize(x = 14.0).sp,
//                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Icon(
                            painter = painterResource(id = R.drawable.motorbike),
                            contentDescription = null
                        )
                    }
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            VerticalDivider(
                modifier = Modifier
                    .height(screenHeight(x = 32.0))
                    .padding(
                        horizontal = screenWidth(x = 22.0)
                    )
            )
            if(orderData.courier != null) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            tint = MaterialTheme.colorScheme.onBackground,
                            painter = painterResource(id = R.drawable.motorbike),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Text(
                            text = "Courier:",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Text(
                            text = orderData.courier.username,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                    }
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            tint = MaterialTheme.colorScheme.onBackground,
                            painter = painterResource(id = R.drawable.phone),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Text(
                            text = orderData.courier.phoneNumber,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
//                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Icon(
                            tint = MaterialTheme.colorScheme.onBackground,
                            painter = painterResource(id = R.drawable.email),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Text(
                            text = orderData.courier.email,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                    }
                    if(role == Role.COURIER && orderData.orderStage == "IN_TRANSIT") {
                        Button(onClick = onCompleteDelivery) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Complete delivery",
                                    fontSize = screenFontSize(x = 14.0).sp,
//                            fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                                Icon(
                                    painter = painterResource(id = R.drawable.motorbike),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            } else {
                if(role == Role.MERCHANT) {
                    Button(
                        onClick = { navigateToCourierSelectionScreen(orderData.id.toString()) },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Assign courier",
                                fontSize = screenFontSize(x = 14.0).sp,
//                            fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Icon(
                                painter = painterResource(id = R.drawable.motorbike),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
//        Spacer(modifier = Modifier.height(screenHeight(x = 32.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = orderData.orderStage == "COMPLETE", onClick = { /*TODO*/ })
            Text(
                text = "Complete",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = screenFontSize(x = 14.0).sp,
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 32.0)))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = screenWidth(x = 1.0),
                    color = Color.LightGray,
                    shape = RoundedCornerShape(screenWidth(x = 10.0))
                )
                .padding(screenWidth(x = 16.0))
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Order code: ",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.W300
                    )
                    Text(
                        text = orderData.orderCode,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Business: ",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.W300
                    )
                    Text(
                        text = orderData.business?.name ?: "",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Text(
                    text = orderData.name,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = orderData.description,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = screenFontSize(x = 14.0).sp,
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Text(
                    text = formatIsoDateTime(LocalDateTime.parse(orderData.createdAt)),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if(role == Role.MERCHANT) "Paid amount: " else "Cost: ",
                        fontSize = screenFontSize(x = 14.0).sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.W300
                    )
                    Text(
                        text = formatMoneyValue(orderData.productCost),
                        fontSize = screenFontSize(x = 14.0).sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                if(orderData.business?.owner?.id == userId) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Delivery cost: ",
                            fontSize = screenFontSize(x = 14.0).sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.W300
                        )
                        Text(
                            text = if(orderData.deliveryCost != null) formatMoneyValue(orderData.deliveryCost) else "N/A",
                            fontSize = screenFontSize(x = 14.0).sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if(orderData.buyer?.id != userId) {
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            tint = MaterialTheme.colorScheme.onBackground,
                            painter = painterResource(id = R.drawable.buyer),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Text(
                            text = "Buyer:",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Text(
                            text = orderData.buyer?.username ?: "",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                    }
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            tint = MaterialTheme.colorScheme.onBackground,
                            painter = painterResource(id = R.drawable.phone),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Text(
                            text = orderData.buyer?.phoneNumber ?: "",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
//                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Icon(
                            tint = MaterialTheme.colorScheme.onBackground,
                            painter = painterResource(id = R.drawable.email),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Text(
                            text = orderData.buyer?.email ?: "",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CompleteDeliveryConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    orderName: String,
    buyerName: String,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Delivery confirmation",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Are you sure you want to complete the delivery of $orderName to $buyerName?",
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
fun CompleteDeliverySuccessDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    orderName: String,
    buyerName: String,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Delivery completed",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "$orderName has been delivered to $buyerName",
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrderDetailsScreenPreview() {
    WazipayTheme {
        OrderDetailsScreen(
            fromPaymentScreen = false,
            userId = 1,
            orderData = orderData,
            role = Role.BUYER,
            navigateToPreviousScreen = {},
            navigateToDashboardScreen = {},
            navigateToCourierSelectionScreen = {},
            onCompleteDelivery = {}
        )
    }
}