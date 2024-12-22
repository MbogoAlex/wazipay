package com.escrow.wazipay.data.network.models.common

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestBody(
    val phoneNumber: String,
    val pin: String,
)
