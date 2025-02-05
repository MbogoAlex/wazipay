package com.escrow.wazipay.data.network.models.business

import com.escrow.wazipay.data.network.models.user.UserContactData
import kotlinx.serialization.Serializable

@Serializable
data class BusinessData(
    val id: Int,
    val name: String,
    val description: String,
    val products: List<ProductData>,
    val createdAt: String,
    val archived: Boolean,
    val archivedAt: String?,
    val owner: UserContactData?
)
