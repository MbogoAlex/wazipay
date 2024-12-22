package com.escrow.wazipay.data.network.models.common

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseBody(
    val statusCode: Int,
    val message: String,
    val data: LoginResponseBodyData
)

@Serializable
data class LoginResponseBodyData(
    val user: LoginResponseBodyDataUser,
    val token: String
)

@Serializable
data class LoginResponseBodyDataUser(
    val userId: Int,
    val name: String,
    val phoneNumber: String,
    val email: String,
    val roles: List<String>,
    val createdAt: String,
    val verified: Boolean,
    val verifiedAt: String,
    val verificationStatus: String,
    val verificationDetails: UserVerificationDetails?,
    val suspended: Boolean,
    val archived: Boolean,
    val archivedAt: String?
)


