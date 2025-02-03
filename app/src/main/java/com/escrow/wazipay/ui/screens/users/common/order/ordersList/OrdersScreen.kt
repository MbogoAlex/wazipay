package com.escrow.wazipay.ui.screens.users.common.order.ordersList

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.data.network.models.order.OrderData
import com.escrow.wazipay.data.network.models.order.orders
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.screens.users.common.order.LoadOrdersStatus
import com.escrow.wazipay.ui.screens.users.common.order.OrderItemComposable
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

object OrdersScreenDestination: AppNavigation {
    override val title: String = "Orders screen"
    override val route: String = "orders-screen"
    val businessId: String = "businessId"
    val stage: String = "stage"
    val routeWithBusinessId: String = "$route/{$businessId}"
    val routeWithStage: String = "$route/status/{$stage}"
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrdersScreenComposable(
    showBackArrow: Boolean = true,
    navigateToLoginScreenWithArgs: (phoneNumber: String, pin: String) -> Unit,
    navigateToOrderDetailsScreen: (orderId: String, fromPaymentScreen: Boolean) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {

    val viewModel: OrdersViewModel = viewModel(factory = AppViewModelFactory.Factory)
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
                viewModel.getOrdersScreenData()
            }
        }
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.loadOrdersStatus == LoadOrdersStatus.LOADING,
        onRefresh = {
            viewModel.getOrdersScreenData()

        }
    )

    if(uiState.unauthorized && uiState.loadOrdersStatus == LoadOrdersStatus.FAIL) {
        navigateToLoginScreenWithArgs(uiState.userDetails.phoneNumber!!, uiState.userDetails.pin!!)
        viewModel.resetStatus()
    }

    Box(
        modifier = modifier
            .safeDrawingPadding()
    ) {
        OrdersScreen(
            pullRefreshState = pullRefreshState,
            showBackArrow = showBackArrow,
            orders = uiState.orders,
            stages = uiState.stages,
            code = uiState.code ?: "",
            onChangeCodeQuery = {
                if(it.isEmpty()) {
                    viewModel.changeCode(null)
                } else {
                    viewModel.changeCode(it)
                }
            },
            onClearCodeQuery = {
                viewModel.changeCode(null)
            },
            selectedStage = uiState.selectedStage,
            onChangeOrderStage = {
                viewModel.changeOrderStage(it)
            },
            role = uiState.role,
            navigateToOrderDetailsScreen = navigateToOrderDetailsScreen,
            navigateToPreviousScreen = navigateToPreviousScreen,
            loadOrdersStatus = uiState.loadOrdersStatus
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrdersScreen(
    pullRefreshState: PullRefreshState?,
    showBackArrow: Boolean,
    role: Role,
    orders: List<OrderData>,
    stages: List<String>,
    selectedStage: String,
    code: String,
    onChangeCodeQuery: (text: String) -> Unit,
    onClearCodeQuery: () -> Unit,
    onChangeOrderStage: (orderStage: String) -> Unit,
    navigateToOrderDetailsScreen: (orderId: String, fromPaymentScreen: Boolean) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    loadOrdersStatus: LoadOrdersStatus,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            if(role == Role.MERCHANT || role == Role.BUYER) {
//                FloatingActionButton(onClick = { /*TODO*/ }) {
//                    Icon(
//                        imageVector = Icons.Default.Add,
//                        contentDescription = "Create Order"
//                    )
//                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            Column(
                modifier = if(!showBackArrow && role == Role.COURIER) Modifier
                    .fillMaxSize()
                    .padding(
                        start = screenWidth(x = 16.0),
                        top = screenHeight(x = 0.0),
                        end = screenWidth(x = 16.0),
                        bottom = screenHeight(x = 8.0)
                    ) else Modifier
                    .fillMaxSize()
                    .padding(
                        vertical = screenHeight(x = 16.0),
                        horizontal = screenWidth(x = 16.0)
                    )
            ) {
                if(showBackArrow) {
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
                            text = "${if(role == Role.BUYER) "My Orders" else if(role == Role.MERCHANT) "Received Orders" else "Assigned Orders"} / $selectedStage",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 16.0).sp
                        )
                    }
                    Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                }

                if(role == Role.COURIER) {
                    TextField(
                        shape = RoundedCornerShape(screenWidth(x = 10.0)),
                        label = {
                            Text(
                                text = "Enter order code",
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        value = code,
                        trailingIcon = {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.inverseOnSurface)
                                    .padding(screenWidth(x = 5.0))
                                    .clickable {
                                        onClearCodeQuery()
                                    }

                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear search",
                                    modifier = Modifier
                                        .size(screenWidth(x = 16.0))
                                )
                            }

                        },
                        onValueChange = onChangeCodeQuery,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                }

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

                if(loadOrdersStatus == LoadOrdersStatus.LOADING) {
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
                    LazyColumn {
                        items(orders) { order ->
                            OrderItemComposable(
                                homeScreen = false,
                                orderData = order,
                                navigateToOrderDetailsScreen = navigateToOrderDetailsScreen,
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

}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrdersScreenPreview() {
    WazipayTheme {
        val stages = listOf("All", "Completed", "In Transit", "Pending pickup", "Cancelled", "Refunded")
        OrdersScreen(
            pullRefreshState = null,
            showBackArrow = true,
            orders = orders,
            selectedStage = "All",
            stages = stages,
            code = "",
            onChangeCodeQuery = {},
            onClearCodeQuery = {},
            onChangeOrderStage = {},
            role = Role.BUYER,
            navigateToOrderDetailsScreen = {orderId, fromPaymentScreen ->

            },
            navigateToPreviousScreen = {},
            loadOrdersStatus = LoadOrdersStatus.INITIAL
        )
    }
}