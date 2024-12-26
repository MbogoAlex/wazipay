package com.escrow.wazipay.data.network.models.wallet

import com.escrow.wazipay.data.network.models.user.UserContactData
import kotlinx.serialization.Serializable

@Serializable
data class UserWalletData(
    val id: Int,
    val balance: Double,
    val owner: UserContactData
)
