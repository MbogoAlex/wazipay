package com.escrow.wazipay.ui.general.order

import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails

data class OrderDetailsUiData(
    val userDetails: UserDetails = UserDetails(),
    val role: Role = Role.BUYER,
    val loadOrdersStatus: LoadOrdersStatus = LoadOrdersStatus.INITIAL
)
