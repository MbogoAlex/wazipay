package com.escrow.wazipay.data.network.models.transaction

import kotlinx.serialization.Serializable

@Serializable
data class TransactionStatusResponseBody (
    val statusCode: Int,
    val message: String,
    val data: TransactionStatusResponseData
)