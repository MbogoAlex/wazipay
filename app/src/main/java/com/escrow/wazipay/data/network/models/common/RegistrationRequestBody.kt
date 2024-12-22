package com.escrow.wazipay.data.network.models.common

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequestBody(
    val name: String,
    val phoneNumber: String,
    val email: String,
)
