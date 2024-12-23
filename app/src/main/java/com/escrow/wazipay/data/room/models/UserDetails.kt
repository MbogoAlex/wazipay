package com.escrow.wazipay.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserDetails(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int = 0,
    val username: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val pin: String? = null,
    val token: String? = null,
)
