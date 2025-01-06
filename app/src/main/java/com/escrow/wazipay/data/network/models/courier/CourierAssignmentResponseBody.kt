package com.escrow.wazipay.data.network.models.courier

import kotlinx.serialization.Serializable

@Serializable
data class CourierAssignmentResponseBody(
    val statusCode: Int,
    val message: String,
    val data: CourierAssignmentData
)
