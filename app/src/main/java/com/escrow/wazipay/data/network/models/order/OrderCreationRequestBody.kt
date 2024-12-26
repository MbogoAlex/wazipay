package com.escrow.wazipay.data.network.models.order

import kotlinx.serialization.Serializable

@Serializable
data class OrderCreationRequestBody(
    val name: String,
    val description: String,
    val amount: Double,
    val businessId: Int
)
