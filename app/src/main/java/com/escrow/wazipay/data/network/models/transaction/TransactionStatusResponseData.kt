package com.escrow.wazipay.data.network.models.transaction

import kotlinx.serialization.Serializable

@Serializable
data class TransactionStatusResponseData(
    val status: String,
    val transactionID: String,
    val amount: String,
    val accountNumber: String,
    val sovNarration: String?,
    val sovTransactionID: String?,
    val partnerReferenceID: String,
)
