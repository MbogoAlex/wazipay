package com.escrow.wazipay.data.network.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserDetailsResponseBody(
    val statusCode: Int,
    val message: String,
    val data: UserDetailsData
)
