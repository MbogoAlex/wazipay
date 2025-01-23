package com.escrow.wazipay.data.network.models.invoice

import kotlinx.serialization.Serializable

@Serializable
data class InvoicePaymentResponseBody(
    val statusCode: Int,
    val message: String,
    val data: InvoiceResponseData
)
