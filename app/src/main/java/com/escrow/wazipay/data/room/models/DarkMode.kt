package com.escrow.wazipay.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DarkMode(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val darkMode: Boolean = false
)