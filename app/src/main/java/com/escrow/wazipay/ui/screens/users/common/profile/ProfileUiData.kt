package com.escrow.wazipay.ui.screens.users.common.profile

import com.escrow.wazipay.data.network.models.user.UserDetailsData
import com.escrow.wazipay.data.network.models.user.emptyUser
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails

data class ProfileUiData(
    val userDetails: UserDetails = UserDetails(),
    val role: Role = Role.BUYER,
    val unauthorized: Boolean = false,
    val userDetailsData: UserDetailsData = emptyUser
)
