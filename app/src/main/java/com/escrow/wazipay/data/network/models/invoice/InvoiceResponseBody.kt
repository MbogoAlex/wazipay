package com.escrow.wazipay.data.network.models.invoice

import kotlinx.serialization.Serializable

@Serializable
data class InvoiceResponseBody(
    val statusCode: Int,
    val message: String,
    val data: InvoiceData
)
