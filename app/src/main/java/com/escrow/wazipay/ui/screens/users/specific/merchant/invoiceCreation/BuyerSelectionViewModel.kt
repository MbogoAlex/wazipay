package com.escrow.wazipay.ui.screens.users.specific.merchant.invoiceCreation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.data.room.repository.DBRepository
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.CourierSelectionScreenDestination
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.CourierSelectionUiData
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.LoadingStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BuyerSelectionViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(BuyerSelectionUiData())
    val uiState: StateFlow<BuyerSelectionUiData> = _uiState.asStateFlow()

    fun changeQuery(text: String) {
        _uiState.update {
            it.copy(
                searchQuery = text
            )
        }

        getBuyers()
    }

    private fun getBuyers() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.getUsers(
                    token = uiState.value.userDetails.token!!,
                    query = uiState.value.searchQuery.ifEmpty { null },
                    verificationStatus = null,
                    startDate = null,
                    endDate = null
                )
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            users = response.body()?.data!!.filter { user ->
                                user.roles.contains("BUYER") && user.userId != uiState.value.userDetails.userId
                            },
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAIL
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.getUsers().collect {users ->
                    _uiState.update {
                        it.copy(
                            userDetails = if(users.isEmpty()) UserDetails() else users[0],
                        )
                    }
                }
            }
        }
    }

//    private fun loadStartUpData() {
//        viewModelScope.launch {
//            while(uiState.value.userDetails.userId == 0) {
//                delay(1000)
//            }
//            getOrder()
//        }
//    }


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

    init {
        _uiState.update {
            it.copy(
                businessId = savedStateHandle[BuyerSelectionScreenDestination.businessId]
            )
        }
        getUser()
        getUserRole()
//        loadStartUpData()
    }
}