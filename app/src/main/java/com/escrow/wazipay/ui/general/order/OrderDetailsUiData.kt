package com.escrow.wazipay.ui.general.order

import com.escrow.wazipay.data.network.models.order.OrderData
import com.escrow.wazipay.data.network.models.order.orders
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails

data class OrderDetailsUiData(
    val userDetails: UserDetails = UserDetails(),
    val role: Role = Role.BUYER,
    val orderData: OrderData = orders[0],
    val unauthorized: Boolean = false,
    val loadOrdersStatus: LoadOrdersStatus = LoadOrdersStatus.INITIAL
)
