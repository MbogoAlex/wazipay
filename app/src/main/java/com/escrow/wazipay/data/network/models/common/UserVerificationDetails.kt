package com.escrow.wazipay.data.network.models.common

import kotlinx.serialization.Serializable

@Serializable
data class UserVerificationDetails(
    val verificationId: Int?,
    val userId: Int?,
    val username: String?,
    val phoneNumber: String?,
    val email: String?,
    val idFront: String?,
    val idBack: String?,
    val verificationStatus: String?,
    val verifiedAt: String?,
)
