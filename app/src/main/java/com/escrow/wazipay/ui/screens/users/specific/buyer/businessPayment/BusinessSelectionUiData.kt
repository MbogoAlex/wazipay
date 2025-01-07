package com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment

import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.ui.screens.users.common.business.LoadBusinessStatus

data class BusinessSelectionUiData(
    val userDetails: UserDetails = UserDetails(),
    val role: Role = Role.BUYER,
    val searchQuery: String? = null,
    val businesses: List<BusinessData> = emptyList(),
    val unauthorized: Boolean = false,
    val loadBusinessStatus: LoadBusinessStatus = LoadBusinessStatus.INITIAL
)
