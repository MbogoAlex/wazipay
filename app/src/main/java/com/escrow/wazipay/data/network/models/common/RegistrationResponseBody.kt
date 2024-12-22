package com.escrow.wazipay.data.network.models.common

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationResponseBody(
    val statusCode: Int,
    val message: String,
    val data: RegistrationResponseBodyData
)

@Serializable
data class RegistrationResponseBodyData(
    val userId: Int,
    val name: String,
    val phoneNumber: String,
    val email: String,
    val roles: List<String>,
    val createdAt: String,
    val verified: Boolean,
    val verifiedAt: String?,
    val verificationStatus: String,
    val verificationDetails: UserVerificationDetails?,
    val suspended: Boolean,
    val archived: Boolean,
    val archivedAt: String?
)