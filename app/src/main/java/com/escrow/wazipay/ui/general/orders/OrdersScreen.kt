package com.escrow.wazipay.ui.general.orders

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.data.network.models.order.OrderData
import com.escrow.wazipay.data.network.models.order.orderData
import com.escrow.wazipay.data.network.models.order.orders
import com.escrow.wazipay.ui.buyer.LoadUserStatus
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrdersScreenComposable(
    profile: String,
    navigateToLoginScreenWithArgs: (phoneNumber: String, pin: String) -> Unit,
    modifier: Modifier = Modifier
) {

    val viewModel: OrdersViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    if(uiState.unauthorized && uiState.loadOrdersStatus == LoadOrdersStatus.FAIL) {
        navigateToLoginScreenWithArgs(uiState.userDetails.phoneNumber!!, uiState.userDetails.pin!!)
        viewModel.resetStatus()
    }

    Box(
        modifier = modifier
            .safeDrawingPadding()
    ) {
        OrdersScreen(
            orders = uiState.orders,
            orderStage = uiState.orderStage,
            onChangeOrderStage = {
                viewModel.changeOrderStage(it)
            },
            profile = profile
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrdersScreen(
    profile: String,
    orders: List<OrderData>,
    orderStage: OrderStage,
    onChangeOrderStage: (orderStage: OrderStage) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            if(profile == "Merchant" || profile == "Buyer") {
                FloatingActionButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create Order"
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        vertical = screenHeight(x = 16.0),
                        horizontal = screenWidth(x = 16.0)
                    )
            ) {
                Text(
                    text = if(profile == "Buyer") "My Orders" else if(profile == "Merchant") "Created Orders" else "My Orders",
                    fontWeight = FontWeight.Bold,
                    fontSize = screenFontSize(x = 16.0).sp
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    if(orderStage == OrderStage.All) {
                        Button(onClick = {
                            onChangeOrderStage(OrderStage.All)
                        }) {
                            Text(text = "All")
                        }
                    } else {
                        OutlinedButton(onClick = {
                            onChangeOrderStage(OrderStage.All)
                        }) {
                            Text(text = "All")
                        }
                    }
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))

                    if(orderStage == OrderStage.COMPLETE) {
                        Button(onClick = {
                            onChangeOrderStage(OrderStage.COMPLETE)
                        }) {
                            Text(text = "Completed")
                        }
                    } else {
                        OutlinedButton(onClick = {
                            onChangeOrderStage(OrderStage.COMPLETE)
                        }) {
                            Text(text = "Completed")
                        }
                    }
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))

                    if(orderStage == OrderStage.IN_TRANSIT) {
                        Button(onClick = {
                            onChangeOrderStage(OrderStage.IN_TRANSIT)
                        }) {
                            Text(text = "In Transit")
                        }
                    } else {
                        OutlinedButton(onClick = {
                            onChangeOrderStage(OrderStage.IN_TRANSIT)
                        }) {
                            Text(text = "In Transit")
                        }
                    }
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))

                    if(orderStage == OrderStage.PENDING_PICKUP) {
                        Button(onClick = {
                            onChangeOrderStage(OrderStage.PENDING_PICKUP)
                        }) {
                            Text(text = "Pending pickup")
                        }
                    } else {
                        OutlinedButton(onClick = {
                            onChangeOrderStage(OrderStage.PENDING_PICKUP)
                        }) {
                            Text(text = "Pending pickup")
                        }
                    }

                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))

                    if(orderStage == OrderStage.CANCELLED) {
                        Button(onClick = {
                            onChangeOrderStage(OrderStage.CANCELLED)
                        }) {
                            Text(text = "Cancelled")
                        }
                    } else {
                        OutlinedButton(onClick = {
                            onChangeOrderStage(OrderStage.CANCELLED)
                        }) {
                            Text(text = "Cancelled")
                        }
                    }

                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))

                    if(orderStage == OrderStage.REFUNDED) {
                        Button(onClick = {
                            onChangeOrderStage(OrderStage.REFUNDED)
                        }) {
                            Text(text = "Refunded")
                        }
                    } else {
                        OutlinedButton(onClick = {
                            onChangeOrderStage(OrderStage.REFUNDED)
                        }) {
                            Text(text = "Refunded")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                LazyColumn {
                    items(orders) { order ->
                        OrderItemComposable(
                            homeScreen = false,
                            orderData = order,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    screenWidth(x = 8.0)
                                )
                        )
                    }
                }

            }

        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrdersScreenPreview() {
    WazipayTheme {
        OrdersScreen(
            orders = orders,
            orderStage = OrderStage.All,
            onChangeOrderStage = {},
            profile = "Buyer"
        )
    }
}