package com.escrow.wazipay.ui.screens.users.common.business.businessList

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails
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

class BusinessViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _uiState = MutableStateFlow(BusinessUiData())
    val uiState: StateFlow<BusinessUiData> = _uiState.asStateFlow()

    private val ownerId: String? = savedStateHandle[BusinessesScreenDestination.ownerId]

    fun updateSearchQuery(query: String?) {
        _uiState.update {
            it.copy(
                searchQuery = query
            )
        }
        getBusinesses()
    }

    private fun getBusinesses() {
        viewModelScope.launch {
            try {
               val response = apiRepository.getBusinesses(
                   token = uiState.value.userDetails.token!!,
                   query = uiState.value.searchQuery,
                   ownerId = ownerId?.toInt()
                       ?: if(uiState.value.userRole.role == Role.MERCHANT) uiState.value.userDetails.userId else null,
                   archived = false,
                   startDate = null,
                   endDate = null
               )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            businesses = if(uiState.value.userRole.role == Role.BUYER) response.body()?.data!!.filter { business ->
                                business.owner?.id != uiState.value.userDetails.userId
                            } else response.body()?.data!!,
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
                    Log.e("loadBusinessesResponse_err", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadBusinessStatus = LoadBusinessStatus.FAIL
                    )
                }
                Log.e("loadBusinessesException_err", e.toString())
            }
        }
    }

    private fun getUserDetails() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.getUsers().collect { users ->
                    _uiState.update {
                        it.copy(
                            userDetails = if(users.isEmpty()) UserDetails() else users[0]
                        )
                    }
                }
            }
        }
    }

    fun getBusinessesScreenData() {
        viewModelScope.launch {
            while (uiState.value.userDetails.userId == 0) {
                delay(1000)
            }
            getBusinesses()
        }
    }

    private fun getUserRole() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.getUserRole().collect { userRole ->
                    _uiState.update {
                        it.copy(
                            userRole = userRole!!
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
        getUserDetails()
        getUserRole()
        getBusinessesScreenData()
    }
}