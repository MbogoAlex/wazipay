package com.escrow.wazipay.ui.auth

enum class PinSetStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    FAIL
}

data class PinUiData(
    val pin: String = "",
    val buttonEnabled: Boolean = false,
    val pinSetMessage: String = "",
    val pinSetStatus: PinSetStatus = PinSetStatus.INITIAL
)
