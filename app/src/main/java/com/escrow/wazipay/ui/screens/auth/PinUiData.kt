package com.escrow.wazipay.ui.screens.auth

import com.escrow.wazipay.data.room.models.UserDetails

enum class PinSetStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    FAIL
}

data class PinUiData(
    val userDetails: UserDetails = UserDetails(),
    val pin: String = "",
    val buttonEnabled: Boolean = false,
    val pinSetMessage: String = "",
    val pinSetStatus: PinSetStatus = PinSetStatus.INITIAL
)
