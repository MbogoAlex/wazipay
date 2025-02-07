package com.escrow.wazipay.data.network.models.courier

import com.escrow.wazipay.data.network.models.transaction.TransactionData
import kotlinx.serialization.Serializable

@Serializable
data class PaymentDetails (
    val transactionToken: String?,
    val transactionDetails: TransactionData,
    val partnerReferenceID: String?,
    val transactionID: String?
)