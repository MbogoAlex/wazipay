package com.escrow.wazipay.data.network.models.common

import com.escrow.wazipay.data.network.models.user.UserDetailsData
import com.escrow.wazipay.data.room.models.UserDetails
import kotlinx.serialization.Serializable

@Serializable
data class UserVerificationData(
    val id: Int,
    val userId: Int,
    val username: String,
    val email: String,
    val verified: Boolean?,
    val verificationStatus: String,
    val createdAt: String,
    val verifiedAt: String?,
    val roles: List<String>,
    val idFront: String,
    val idBack: String,
    val verifiedBy: UserDetailsData,
)
