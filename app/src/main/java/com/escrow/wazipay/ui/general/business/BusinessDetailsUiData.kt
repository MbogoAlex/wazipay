package com.escrow.wazipay.ui.general.business

import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.business.businesses
import com.escrow.wazipay.data.room.models.UserDetails

data class BusinessDetailsUiData(
    val userDetails: UserDetails = UserDetails(),
    val businessData: BusinessData = businesses[0],
    val unauthorized: Boolean = false,
    val loadBusinessStatus: LoadBusinessStatus = LoadBusinessStatus.INITIAL
)
