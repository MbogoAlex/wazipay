package com.escrow.wazipay.data.network.models.business

import kotlinx.serialization.Serializable

@Serializable
data class BusinessResponseBody(
    val statusCode: Int,
    val message: String,
    val data: BusinessData
)
