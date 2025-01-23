package com.escrow.wazipay.data.network.models.transaction

import kotlinx.serialization.Serializable

@Serializable
data class TransactionStatusRequestBody(
    val referenceId: String,
    val transactionId: String,
    val token: String
)
