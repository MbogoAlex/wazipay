package com.escrow.wazipay.ui.general.business

import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.data.room.models.UserRole

data class BusinessUiData(
    val userDetails: UserDetails = UserDetails(),
    val userRole: UserRole = UserRole(0, Role.BUYER),
    val searchQuery: String? = null,
    val loadBusinessStatus: LoadBusinessStatus = LoadBusinessStatus.INITIAL,
    val unauthorized: Boolean = false,
    val businesses: List<BusinessData> = emptyList()
)
