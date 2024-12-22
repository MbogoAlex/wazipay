package com.escrow.wazipay.data.network.models.common

import kotlinx.serialization.Serializable

@Serializable
data class SetPinResponseBody(
    val statusCode: Int,
    val message: String,
    val data: SetPinResponseBodyData
)

@Serializable
data class SetPinResponseBodyData(
    val userId: Int,
    val name: String,
    val phoneNumber: String,
    val email: String,
    val roles: List<String>,
    val createdAt: String
)
