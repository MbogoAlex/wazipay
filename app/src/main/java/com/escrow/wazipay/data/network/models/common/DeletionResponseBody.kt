package com.escrow.wazipay.data.network.models.common

import kotlinx.serialization.Serializable

@Serializable
data class DeletionResponseBody(
    val statusCode: Int,
    val message: String,
    val data: String
)
