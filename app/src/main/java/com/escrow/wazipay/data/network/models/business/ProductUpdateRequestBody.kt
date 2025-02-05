package com.escrow.wazipay.data.network.models.business

import kotlinx.serialization.Serializable

@Serializable
data class ProductUpdateRequestBody(
    val productId: Int,
    val name: String,
)
