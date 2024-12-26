package com.escrow.wazipay.data.network.models.transaction

import kotlinx.serialization.Serializable

@Serializable
data class TransactionsResponseBody(
    val statusCode: Int,
    val message: String,
    val data: List<TransactionData>
)
