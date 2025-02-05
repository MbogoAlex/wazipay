package com.escrow.wazipay.data.network.models.business

import kotlinx.serialization.Serializable

@Serializable
data class ProductResponseBody(
    val statusCode: Int,
    val message: String,
    val data: ProductData
)
