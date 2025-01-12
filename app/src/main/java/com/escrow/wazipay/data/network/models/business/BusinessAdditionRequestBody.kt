package com.escrow.wazipay.data.network.models.business

import kotlinx.serialization.Serializable

@Serializable
data class BusinessAdditionRequestBody(
    val name: String,
    val description: String,
    val location: String,
    val products: List<String>,
)
