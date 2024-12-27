package com.escrow.wazipay.data.network.models.wallet

import kotlinx.serialization.Serializable

@Serializable
data class WithdrawalRequestBody(
    val amount: Double,
)
