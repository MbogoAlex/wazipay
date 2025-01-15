package com.escrow.wazipay.data.network.models.transaction

import kotlinx.serialization.Serializable

@Serializable
data class TransactionStatus(
    val status: Boolean
)
