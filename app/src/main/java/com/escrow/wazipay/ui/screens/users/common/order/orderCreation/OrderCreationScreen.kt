package com.escrow.wazipay.ui.screens.users.common.order.orderCreation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.business.businessData
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.screens.users.common.NavBarItem
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.formatMoneyValue
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

object OrderCreationScreenDestination: AppNavigation {
    override val title: String = "Order creation screen"
    override val route: String = "order-creation-screen"
    val businessId: String = "businessId"
    val routeWithArgs: String = "$route/{$businessId}"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderCreationScreenComposable(
    navigateToDashboardWithChildScreen: (child: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: OrderCreationViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    var showConfirmDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var showSuccessDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if(uiState.orderCreationStatus == OrderCreationStatus.SUCCESS) {
        showSuccessDialog = true
    }

    if(showConfirmDialog) {
        OrderConfirmationDialog(
            onConfirm = {
                viewModel.createOrder()
                showConfirmDialog = !showConfirmDialog
            },
            onDismiss = {
                showConfirmDialog = !showConfirmDialog
            },
            order = uiState.productName,
            cost = formatMoneyValue(uiState.amount.toDouble())
        )
    }

    if(showSuccessDialog) {
        OrderCreationSuccessDialog(
            onConfirm = {
                viewModel.resetStatus()
                navigateToDashboardWithChildScreen(NavBarItem.ORDERS.name)
            },
            onDismiss = {
                viewModel.resetStatus()
                navigateToDashboardWithChildScreen(NavBarItem.ORDERS.name)
            },
            order = uiState.productName,
            cost = formatMoneyValue(uiState.amount.toDouble())
        )
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OrderCreationScreen(
            userId = uiState.userDetails.userId,
            productName = uiState.productName,
            onChangeProductName = {
                viewModel.updateName(it)
                viewModel.enableButton()
            },
            productDescription = uiState.description,
            onChangeProductDescription = {
                viewModel.updateDescription(it)
                viewModel.enableButton()
            },
            productAmount = uiState.amount,
            onChangeProductAmount = { value ->
                val validValue = value.filter { it.isDigit() }
                viewModel.updateAmount(validValue)
                viewModel.enableButton()
            },
            businessData = uiState.businessData,
            buttonEnabled = uiState.buttonEnabled,
            orderCreationStatus = uiState.orderCreationStatus,
            onCreateOrder = {
                showConfirmDialog = !showConfirmDialog
            },
            navigateToPreviousScreen = navigateToPreviousScreen
        )
    }

}

@Composable
fun OrderCreationScreen(
    userId: Int,
    productName: String,
    onChangeProductName: (name: String) -> Unit,
    productDescription: String,
    onChangeProductDescription: (description: String) -> Unit,
    productAmount: String,
    onChangeProductAmount: (amount: String) -> Unit,
    businessData: BusinessData,
    buttonEnabled: Boolean,
    orderCreationStatus: OrderCreationStatus,
    onCreateOrder: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
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
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                enabled = orderCreationStatus != OrderCreationStatus.LOADING,
                onClick = navigateToPreviousScreen
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Text(
                text = "New Order",
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 14.0).sp
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        TextField(
            label = {
                Text(
                    text = "Products Name",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            },
            value = productName,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onChangeProductName,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        TextField(
            label = {
                Text(
                    text = "Description",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            },
            value = productDescription,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onChangeProductDescription,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        TextField(
            label = {
                Text(
                    text = "Amount",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            },
            value = productAmount,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.NumberPassword
            ),
            onValueChange = onChangeProductAmount,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = "Business",
            fontWeight = FontWeight.Bold,
            fontSize = screenFontSize(x = 14.0).sp
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(screenWidth(x = 16.0))
            ) {
                if(businessData.owner?.id == userId) {
                    Text(
                        text = "My Business",
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(screenWidth(x = 4.0)))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.shop),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = businessData.name,
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Text(
                    text = businessData.description,
                    fontSize = screenFontSize(x = 14.0).sp,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 4.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.person),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(text = businessData.owner?.username ?: "")
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    Icon(
                        painter = painterResource(id = R.drawable.phone),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(text = businessData.owner?.phoneNumber ?: "")
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            enabled = buttonEnabled && orderCreationStatus != OrderCreationStatus.LOADING,
            onClick = onCreateOrder,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if(orderCreationStatus == OrderCreationStatus.LOADING) {
                Text(
                    text = "Loading...",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            } else {
                Text(
                    text = "Confirm order",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    }
}

@Composable
fun OrderConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    order: String,
    cost: String,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Confirm New Order",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Are you sure you want to create a new order for $order with a cost of $cost?",
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
fun OrderCreationSuccessDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    order: String,
    cost: String,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Order made",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Your order for $order with a cost of $cost has been made successfully",
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrderCreationScreenPreview() {
    WazipayTheme {
        OrderCreationScreen(
            userId = 1,
            productName = "",
            onChangeProductName = {},
            productDescription = "",
            onChangeProductDescription = {},
            productAmount = "",
            onChangeProductAmount = {},
            businessData = businessData,
            buttonEnabled = false,
            orderCreationStatus = OrderCreationStatus.INITIAL,
            onCreateOrder = { /*TODO*/ },
            navigateToPreviousScreen = { /*TODO*/ }
        )
    }
}