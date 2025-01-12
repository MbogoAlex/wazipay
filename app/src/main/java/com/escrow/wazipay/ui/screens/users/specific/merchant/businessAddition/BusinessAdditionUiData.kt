package com.escrow.wazipay.ui.screens.users.specific.merchant.businessAddition

import com.escrow.wazipay.data.network.models.user.UserContactData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.LoadingStatus

data class BusinessAdditionUiData(
    val userDetails: UserDetails = UserDetails(),
    val userContactData: UserContactData = UserContactData(1, "", "", ""),
    val role: Role = Role.BUYER,
    val name: String = "",
    val description: String = "",
    val location: String = "",
    val products: List<String> = listOf(""),
    val buttonEnabled: Boolean = false,
    val businessId: Int = 0,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
