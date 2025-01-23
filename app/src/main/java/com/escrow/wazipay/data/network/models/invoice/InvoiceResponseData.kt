package com.escrow.wazipay.data.network.models.invoice

import kotlinx.serialization.Serializable

@Serializable
data class InvoiceResponseData(
    val transactionToken: String,
    val transactionDetails: InvoiceData,
    val partnerReferenceID: String,
    val transactionID: String,
)
