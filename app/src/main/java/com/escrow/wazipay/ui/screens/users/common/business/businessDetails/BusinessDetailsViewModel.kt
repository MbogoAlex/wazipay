package com.escrow.wazipay.ui.screens.users.common.business.businessDetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.repository.DBRepository
import com.escrow.wazipay.ui.screens.users.common.business.LoadBusinessStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BusinessDetailsViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _uiState = MutableStateFlow(BusinessDetailsUiData())
    val uiState: StateFlow<BusinessDetailsUiData> = _uiState.asStateFlow()

    private fun getBusiness() {
        viewModelScope.launch {
            try {
                val response = apiRepository.getBusiness(
                    token = uiState.value.userDetails.token!!,
                    businessId = if(uiState.value.businessId != null) uiState.value.businessId!!.toInt() else 1
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            businessData = response.body()?.data!!,
                            loadBusinessStatus = LoadBusinessStatus.SUCCESS
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
                    _uiState.update {
                        it.copy(
                            loadBusinessStatus = LoadBusinessStatus.FAIL
                        )
                    }
                    Log.e("loadBusinessResponse_err", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadBusinessStatus = LoadBusinessStatus.FAIL
                    )
                }
                Log.e("loadBusinessException_err", e.toString())
            }
        }
    }

    private fun getUserDetails() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.getUsers().collect { users ->
                    _uiState.update {
                        it.copy(
                            userDetails = users[0]
                        )
                    }
                }
            }
        }
    }

    private fun getBusinessScreenData() {
        viewModelScope.launch {
            while (uiState.value.userDetails.userId == 0) {
                delay(1000)
            }
            getBusiness()
        }
    }

    private fun getUserRole() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.getUserRole().collect { userRole ->
                    _uiState.update {
                        it.copy(
                            role = userRole!!.role
                        )
                    }
                }
            }
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                loadBusinessStatus = LoadBusinessStatus.INITIAL
            )
        }
    }

    init {
        _uiState.update {
            it.copy(
                businessId = savedStateHandle[BusinessDetailsScreenDestination.businessId]
            )
        }
        getUserRole()
        getUserDetails()
        getBusinessScreenData()
    }
}