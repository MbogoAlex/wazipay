package com.escrow.wazipay.ui.general.order

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
import com.escrow.wazipay.data.network.models.order.orders
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

object OrdersScreenDestination: AppNavigation {
    override val title: String = "Orders screen"
    override val route: String = "orders-screen"
    val businessId: String = "businessId"
    val routeWithArgs: String = "$route/{$businessId}"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrdersScreenComposable(
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
            stages = uiState.stages,
            selectedStage = uiState.selectedStage,
            onChangeOrderStage = {
                viewModel.changeOrderStage(it)
            },
            role = uiState.role
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrdersScreen(
    role: Role,
    orders: List<OrderData>,
    stages: List<String>,
    selectedStage: String,
    onChangeOrderStage: (orderStage: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            if(role == Role.MERCHANT || role == Role.BUYER) {
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
                    text = "${if(role == Role.BUYER) "My Orders" else if(role == Role.MERCHANT) "Received Orders" else "My Orders"} / $selectedStage",
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
                    stages.forEach { stage ->
                        if(selectedStage == stage) {
                            Button(onClick = { onChangeOrderStage(stage) }) {
                                Text(text = stage)
                            }
                        } else {
                            OutlinedButton(onClick = { onChangeOrderStage(stage) }) {
                                Text(text = stage)
                            }
                        }
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
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
        val stages = listOf("All", "Completed", "In Transit", "Pending pickup", "Cancelled", "Refunded")
        OrdersScreen(
            orders = orders,
            selectedStage = "All",
            stages = stages,
            onChangeOrderStage = {},
            role = Role.BUYER
        )
    }
}