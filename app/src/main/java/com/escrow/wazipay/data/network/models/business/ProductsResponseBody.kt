package com.escrow.wazipay.data.network.models.business

import kotlinx.serialization.Serializable

@Serializable
data class ProductsResponseBody(
    val statusCode: Int,
    val message: String,
    val data: List<ProductData>
)
