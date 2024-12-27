package com.escrow.wazipay.ui.general.orders

import com.escrow.wazipay.data.network.models.order.OrderData
import com.escrow.wazipay.data.room.models.UserDetails

data class OrdersUiData(
    val userDetails: UserDetails = UserDetails(),
    val orders: List<OrderData> = emptyList(),
    val orderStage: OrderStage = OrderStage.All,
    val loadOrdersStatus: LoadOrdersStatus = LoadOrdersStatus.INITIAL,
    val unauthorized: Boolean = true
)
