package com.escrow.wazipay.data.network.models.courier

import kotlinx.serialization.Serializable

@Serializable
data class CourierAssignmentRequestBody(
    val orderId: Int,
    val courierId: Int,
    val deliveryCost: Double
)
