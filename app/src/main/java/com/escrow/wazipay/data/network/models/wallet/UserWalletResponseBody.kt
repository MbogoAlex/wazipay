package com.escrow.wazipay.data.network.models.wallet

import com.escrow.wazipay.data.network.models.user.UserContactData
import kotlinx.serialization.Serializable

@Serializable
data class UserWalletResponseBody(
    val statusCode: Int,
    val message: String,
    val data: UserContactData
)
