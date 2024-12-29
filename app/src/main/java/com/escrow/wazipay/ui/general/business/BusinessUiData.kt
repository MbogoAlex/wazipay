package com.escrow.wazipay.ui.general.business

import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.room.models.UserDetails

data class BusinessUiData(
    val userDetails: UserDetails = UserDetails(),
    val loadBusinessStatus: LoadBusinessStatus = LoadBusinessStatus.INITIAL,
    val unauthorized: Boolean = false,
    val businesses: List<BusinessData> = emptyList()
)
