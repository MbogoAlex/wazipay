package com.escrow.wazipay.ui.merchant.courier

import com.escrow.wazipay.data.network.models.user.UserDetailsData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails

data class CourierSelectionUiData(
    val userDetails: UserDetails = UserDetails(),
    val role: Role = Role.BUYER,
    val users: List<UserDetailsData> = emptyList(),
    val searchQuery: String = "",
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
