package com.escrow.wazipay.data.network.models.common

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequestBody(
    val username: String,
    val phoneNumber: String,
    val email: String,
)
