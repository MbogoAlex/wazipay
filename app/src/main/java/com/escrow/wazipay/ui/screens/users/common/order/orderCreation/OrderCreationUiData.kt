package com.escrow.wazipay.ui.screens.users.common.order.orderCreation

import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.business.businesses
import com.escrow.wazipay.data.room.models.UserDetails

data class OrderCreationUiData(
    val userDetails: UserDetails = UserDetails(),
    val productName: String = "",
    val description: String = "",
    val amount: String = "",
    val businessData: BusinessData = businesses[0],
    val businessId: String? = null,
    val buttonEnabled: Boolean = false,
    val unauthorized: Boolean = false,
    val orderCreationStatus: OrderCreationStatus = OrderCreationStatus.INITIAL
)
