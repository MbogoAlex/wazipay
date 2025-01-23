package com.escrow.wazipay.data.network.models.wallet

import kotlinx.serialization.Serializable

@Serializable
data class DepositResponseBody(
    val statusCode: Int,
    val message: String,
    val data: DepositResponseData
)
