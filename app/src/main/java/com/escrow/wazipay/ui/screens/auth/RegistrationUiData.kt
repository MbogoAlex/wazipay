package com.escrow.wazipay.ui.screens.auth

enum class RegistrationStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    FAIL
}

data class RegistrationUiData(
    val username: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val registrationMessage: String = "",
    val buttonEnabled: Boolean = false,
    val registrationStatus: RegistrationStatus = RegistrationStatus.INITIAL
)
