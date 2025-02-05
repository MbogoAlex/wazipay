package com.escrow.wazipay.data.network.models.business

import kotlinx.serialization.Serializable

@Serializable
data class ProductData(
    val productId: Int,
    val name: String,
    val businessId: Int,
)
