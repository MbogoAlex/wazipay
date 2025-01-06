package com.escrow.wazipay.data.network.models.courier

import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.user.UserContactData
import kotlinx.serialization.Serializable

@Serializable
data class CourierAssignmentData(
    val id: Int,
    val orderCode: String,
    val name: String,
    val description: String,
    val productCost: Double,
    val deliveryCost: Double,
    val createdAt: String,
    val assignedAt: String?,
    val orderStage: String,
    val merchant: UserContactData,
    val buyer: UserContactData,
    val courier: UserContactData,
    val business: BusinessData,
    val completedAt: String?,
    val refundedAt: String?,
    val cancelledAt: String?,
    val refundReason: String?,
    val cancellationReason: String?
)
