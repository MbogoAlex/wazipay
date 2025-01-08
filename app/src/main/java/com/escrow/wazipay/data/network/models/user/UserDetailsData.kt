package com.escrow.wazipay.data.network.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserDetailsData(
    val userId: Int,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val createdAt: String,
    val archived: Boolean,
    val archivedAt: String?,
    val verified: Boolean,
    val verifiedAt: String?,
    val verificationStatus: String,
    val roles: List<String>,
    val idFront: String?,
    val idBack: String?
)
