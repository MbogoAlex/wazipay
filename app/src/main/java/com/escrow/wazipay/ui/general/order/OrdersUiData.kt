package com.escrow.wazipay.ui.general.order

import com.escrow.wazipay.data.network.models.order.OrderData
import com.escrow.wazipay.data.room.models.UserDetails

data class OrdersUiData(
    val userDetails: UserDetails = UserDetails(),
    val orders: List<OrderData> = emptyList(),
    val businessId: String? = null,
    val orderStage: OrderStage = OrderStage.All,
    val selectedStage: String = "All",
    val stages: List<String> = listOf("All", "Completed", "In Transit", "Pending pickup", "Cancelled", "Refunded"),
    val loadOrdersStatus: LoadOrdersStatus = LoadOrdersStatus.INITIAL,
    val unauthorized: Boolean = true
)
