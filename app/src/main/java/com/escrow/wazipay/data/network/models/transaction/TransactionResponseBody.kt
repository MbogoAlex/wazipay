package com.escrow.wazipay.data.network.models.transaction

import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponseBody(
    val statusCode: Int,
    val message: String,
    val data: TransactionData
)
