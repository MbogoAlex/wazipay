package com.escrow.wazipay.data.network.models.common

import kotlinx.serialization.Serializable

@Serializable
data class SetPinRequestBody(
    val userId: Int,
    val pin: String,
)
