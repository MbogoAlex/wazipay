package com.escrow.wazipay.ui.screens.users.specific.merchant.invoiceCreation

import com.escrow.wazipay.data.network.models.user.UserDetailsData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.LoadingStatus

data class BuyerSelectionUiData(
    val userDetails: UserDetails = UserDetails(),
    val role: Role = Role.BUYER,
    val businessId: String? = null,
    val users: List<UserDetailsData> = emptyList(),
    val searchQuery: String = "",
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
