package com.escrow.wazipay.data.network.models.order

import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.user.UserContactData
import kotlinx.serialization.Serializable

@Serializable
data class OrderData(
    val id: Int,
    val orderCode: String,
    val name: String,
    val description: String,
    val productCost: Double,
    val deliveryCost: Double?,
    val orderStage: String,
    val merchant: UserContactData?,
    val buyer: UserContactData?,
    val courier: UserContactData?,
    val courierPaid: Boolean?,
    val business: BusinessData?,
    val createdAt: String,
    val assignedAt: String?,
    val completedAt: String?,
    val refundedAt: String?,
    val cancelledAt: String?,
    val refundReason: String?,
    val cancellationReason: String?
)