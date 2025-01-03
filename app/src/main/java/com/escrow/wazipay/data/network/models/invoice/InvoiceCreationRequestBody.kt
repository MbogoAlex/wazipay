package com.escrow.wazipay.data.network.models.invoice

import kotlinx.serialization.Serializable

@Serializable
data class InvoiceCreationRequestBody(
    val title: String,
    val description: String,
    val amount: Double,
    val businessId: Int,
    val buyerId: Int
)
