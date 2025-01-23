package com.escrow.wazipay.data.network.models.wallet

import kotlinx.serialization.Serializable

@Serializable
data class DepositResponseData(
    val transactionToken: String,
    val transactionDetails: UserWalletData,
    val partnerReferenceID: String,
    val transactionID: String
)
