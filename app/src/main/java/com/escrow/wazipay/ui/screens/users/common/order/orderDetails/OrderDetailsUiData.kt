package com.escrow.wazipay.ui.screens.users.common.order.orderDetails

import com.escrow.wazipay.data.network.models.order.OrderData
import com.escrow.wazipay.data.network.models.order.emptyOrderData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.ui.screens.users.common.order.LoadOrdersStatus

data class OrderDetailsUiData(
    val userDetails: UserDetails = UserDetails(),
    val role: Role = Role.BUYER,
    val orderData: OrderData = emptyOrderData,
    val unauthorized: Boolean = false,
    val fromPaymentScreen: Boolean = false,
    val loadOrdersStatus: LoadOrdersStatus = LoadOrdersStatus.INITIAL
)
