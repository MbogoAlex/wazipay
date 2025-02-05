package com.escrow.wazipay.data.network.models.invoice

import kotlinx.serialization.Serializable

@Serializable
data class InvoiceStatusChangeRequestBody(
    val invoiceStatus: String,
    val invoiceId: Int,
)
