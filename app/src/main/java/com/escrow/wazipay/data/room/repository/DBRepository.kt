package com.escrow.wazipay.data.room.repository

import com.escrow.wazipay.data.room.models.DarkMode
import com.escrow.wazipay.data.room.models.UserDetails
import kotlinx.coroutines.flow.Flow

interface DBRepository {
    fun insertUser(userDetails: UserDetails)

    suspend fun updateUser(userDetails: UserDetails)


    fun getUsers(): Flow<List<UserDetails>>


    fun getUser(userId: Int): Flow<UserDetails>

    suspend fun deleteUsers();

    suspend fun createTheme(darkMode: DarkMode)

    suspend fun changeTheme(darkMode: DarkMode)

    fun getTheme(): Flow<DarkMode?>
}