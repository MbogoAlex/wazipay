package com.escrow.wazipay.data.network.models.courier

import kotlinx.serialization.Serializable

@Serializable
data class CourierPaymentRequest(
    val orderId: Int,
    val courierId: Int,
    val transactionMethod: String,
    val phoneNumber: String,
    val deliveryCost: Double
)
