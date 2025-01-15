package com.escrow.wazipay.ui.screens.users.common.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.data.room.repository.DBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserAccountOverviewViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(UserAccountOverviewUiData())
    val uiState: StateFlow<UserAccountOverviewUiData> = _uiState.asStateFlow()

    private fun loadUserDetails() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.getUsers().collect { users ->
                    _uiState.update {
                        it.copy(
                            userDetails = if(users.isNotEmpty()) users[0] else UserDetails()
                        )
                    }
                }
            }
        }
    }

    private fun getUserDetails() {
        viewModelScope.launch {
            try {
                while(uiState.value.userDetails.userId == 0) {
                    delay(1000)
                }
                val response = apiRepository.getUserDetails(
                    token = uiState.value.userDetails.token!!,
                    userId = uiState.value.userDetails.userId
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            userDetailsData = response.body()?.data!!,
                        )
                    }
                } else {
                    if(response.code() == 401) {
                        _uiState.update {
                            it.copy(
                                unauthorized = true
                            )
                        }
                    }

                    Log.e("getUserResponse_err", response.toString())
                }

            } catch (e: Exception) {

                Log.e("getUserException_err", e.toString())
            }
        }
    }

    fun loadVerificationScreenUiData() {
        viewModelScope.launch {
            while (uiState.value.userDetails.userId == 0) {
                delay(1000)
            }
            getUserDetails()
        }
    }

    init {
        loadUserDetails()
        loadVerificationScreenUiData()
    }
}