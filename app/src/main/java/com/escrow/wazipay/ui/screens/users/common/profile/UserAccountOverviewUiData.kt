package com.escrow.wazipay.ui.screens.users.common.profile

import com.escrow.wazipay.data.network.models.user.UserDetailsData
import com.escrow.wazipay.data.network.models.user.emptyUser
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.LoadingStatus

data class UserAccountOverviewUiData(
    val userDetails: UserDetails = UserDetails(),
    val userDetailsData: UserDetailsData = emptyUser,
    val username: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val buttonEnabled: Boolean = false,
    val unauthorized: Boolean = false,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
