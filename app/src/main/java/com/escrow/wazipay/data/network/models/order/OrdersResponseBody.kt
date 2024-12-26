package com.escrow.wazipay.data.network.models.order

import kotlinx.serialization.Serializable

@Serializable
data class OrdersResponseBody(
    val statusCode: Int,
    val message: String,
    val data: List<OrderData>
)
