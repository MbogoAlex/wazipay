package com.escrow.wazipay.data.network.models.order

import com.escrow.wazipay.data.network.models.user.UserContactData
import kotlinx.serialization.Serializable

@Serializable
data class OrderCreationResponseBody(
    val statusCode: Int,
    val message: String,
    val data: OrderData
)

