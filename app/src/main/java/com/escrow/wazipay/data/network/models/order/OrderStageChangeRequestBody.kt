package com.escrow.wazipay.data.network.models.order

import kotlinx.serialization.Serializable

@Serializable
data class OrderStageChangeRequestBody(
    val orderStage: String,
    val orderId: Int
)
