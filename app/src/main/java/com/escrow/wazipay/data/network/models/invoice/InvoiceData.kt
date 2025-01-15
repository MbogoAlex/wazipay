package com.escrow.wazipay.data.network.models.invoice

import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.transaction.TransactionData
import com.escrow.wazipay.data.network.models.user.UserContactData
import kotlinx.serialization.Serializable

@Serializable
data class InvoiceData(
    val id: Int,
    val title: String,
    val description: String,
    val amount: Double,
    val businessId: Int,
    val merchant: UserContactData,
    val buyer: UserContactData,
    val invoiceStatus: String,
    val createdAt: String,
    val rejectedAt: String?,
    val cancelledAt: String?,
    val rejectionReason: String?,
    val cancellationReason: String?,
    val transactionId: Int?,
    val transactionCode: String?,
    val orderId: Int?,
    val paymentLink: String
)
