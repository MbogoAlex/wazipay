package com.escrow.wazipay.data.network.models.invoice

import com.escrow.wazipay.data.network.models.transaction.TransactionMethod
import kotlinx.serialization.Serializable

@Serializable
data class InvoicePaymentRequestBody(
    val invoiceId: Int,
    val transactionMethod: TransactionMethod,
    val phoneNumber: String,
)
