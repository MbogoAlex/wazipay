package com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment

import com.escrow.wazipay.data.network.models.order.OrderData
import com.escrow.wazipay.data.network.models.order.emptyOrderData
import com.escrow.wazipay.data.network.models.user.UserDetailsData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails

data class CourierSelectionUiData(
    val userDetails: UserDetails = UserDetails(),
    val role: Role = Role.BUYER,
    val users: List<UserDetailsData> = emptyList(),
    val orderId: String? = null,
    val orderData: OrderData = emptyOrderData,
    val searchQuery: String = "",
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
