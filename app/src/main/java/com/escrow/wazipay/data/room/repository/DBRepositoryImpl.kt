package com.escrow.wazipay.data.room.repository

import com.escrow.wazipay.data.room.AppDao
import com.escrow.wazipay.data.room.models.DarkMode
import com.escrow.wazipay.data.room.models.UserDetails
import kotlinx.coroutines.flow.Flow

class DBRepositoryImpl(private val appDao: AppDao): DBRepository {
    override fun insertUser(userDetails: UserDetails) =
        appDao.insertUser(userDetails)

    override suspend fun updateUser(userDetails: UserDetails) =
        appDao.updateUser(userDetails)

    override fun getUsers(): Flow<List<UserDetails>> =
        appDao.getUsers()

    override fun getUser(userId: Int): Flow<UserDetails> =
        appDao.getUser(userId)

    override suspend fun deleteUsers() =
        appDao.deleteUsers()

    override suspend fun createTheme(darkMode: DarkMode) =
        appDao.createTheme(darkMode)

    override suspend fun changeTheme(darkMode: DarkMode) =
        appDao.changeTheme(darkMode)

    override fun getTheme(): Flow<DarkMode?> =
        appDao.getTheme()
}