package com.escrow.wazipay.data.network.models.invoice

import kotlinx.serialization.Serializable

@Serializable
data class InvoicesResponseBody(
    val statusCode: Int,
    val message: String,
    val data: List<InvoiceData>
)
