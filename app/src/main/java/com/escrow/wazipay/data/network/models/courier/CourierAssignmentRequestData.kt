package com.escrow.wazipay.data.network.models.courier

import kotlinx.serialization.Serializable

@Serializable
data class CourierAssignmentRequestData(
    val paymentDetails: PaymentDetails
)
