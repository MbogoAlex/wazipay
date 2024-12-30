package com.escrow.wazipay.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.escrow.wazipay.data.room.models.DarkMode
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.data.room.models.UserRole
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(userDetails: UserDetails)

    @Update
    suspend fun updateUser(userDetails: UserDetails)

    @Query("SELECT * FROM UserDetails")
    fun getUsers(): Flow<List<UserDetails>>

    @Query("SELECT * FROM UserDetails WHERE userId = :userId")
    fun getUser(userId: Int): Flow<UserDetails>

    @Query("DELETE FROM userdetails")
    suspend fun deleteUsers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createTheme(darkMode: DarkMode)

    @Update
    suspend fun changeTheme(darkMode: DarkMode)

    @Query("SELECT * FROM DarkMode LIMIT 1")
    fun getTheme(): Flow<DarkMode?>

    @Insert
    suspend fun insertUserRole(userRole: UserRole)

    @Update
    suspend fun updateUserRole(userRole: UserRole)

    @Query("SELECT * FROM UserRole LIMIT 1")
    fun getUserRole(): Flow<UserRole?>
}