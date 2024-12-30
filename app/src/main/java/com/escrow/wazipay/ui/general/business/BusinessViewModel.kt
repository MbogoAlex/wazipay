package com.escrow.wazipay.ui.general.business

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.repository.DBRepository
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
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(BusinessUiData())
    val uiState: StateFlow<BusinessUiData> = _uiState.asStateFlow()

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
                   ownerId = if(uiState.value.userRole.role == Role.MERCHANT) uiState.value.userDetails.userId else null,
                   archived = null,
                   startDate = null,
                   endDate = null
               )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            businesses = response.body()?.data!!.filter { business ->
                                business.owner.id != uiState.value.userDetails.userId
                            },
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
                            userDetails = users[0]
                        )
                    }
                }
            }
        }
    }

    private fun getBusinessesScreenData() {
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