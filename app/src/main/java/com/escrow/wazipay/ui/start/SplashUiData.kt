package com.escrow.wazipay.ui.start

import com.escrow.wazipay.data.room.models.UserDetails

data class SplashUiData(
    val userDetails: UserDetails = UserDetails(),
    val isLoading: Boolean = true,
    val isNavigating: Boolean = true,
)
