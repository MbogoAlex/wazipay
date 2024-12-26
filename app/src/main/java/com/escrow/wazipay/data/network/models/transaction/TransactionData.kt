package com.escrow.wazipay.data.network.models.transaction

import com.escrow.wazipay.data.network.models.invoice.InvoiceData
import com.escrow.wazipay.data.network.models.order.OrderData
import kotlinx.serialization.Serializable

@Serializable
data class TransactionData(
    val id: Int,
    val transactionCode: String,
    val transactionType: String,
    val createdAt: String?,
    val amount: Double,
    val order: OrderData?,
    val invoice: InvoiceData?
)
