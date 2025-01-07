package com.escrow.wazipay.ui.screens.auth

enum class LoginStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    FAIL
}
data class LoginUiData(
    val phoneNumber: String = "",
    val pin: String = "",
    val loginStatus: LoginStatus = LoginStatus.INITIAL,
    val loginMessage: String = "",
    val buttonEnabled: Boolean = false
)
