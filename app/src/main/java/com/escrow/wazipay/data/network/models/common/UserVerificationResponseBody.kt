package com.escrow.wazipay.data.network.models.common

import kotlinx.serialization.Serializable

@Serializable
data class UserVerificationResponseBody(
    val statusCode: Int,
    val message: String,
    val data: UserVerificationData
)
