package com.escrow.wazipay.ui.screens.users.specific.merchant.businessAddition

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.models.business.BusinessAdditionRequestBody
import com.escrow.wazipay.data.network.models.user.UserContactData
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.data.room.repository.DBRepository
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.LoadingStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BusinessAdditionViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(BusinessAdditionUiData())
    val uiState: StateFlow<BusinessAdditionUiData> = _uiState.asStateFlow()

    fun changeName(name: String) {
        _uiState.update {
            it.copy(
                name = name
            )
        }
    }

    fun changeDescription(description: String) {
        _uiState.update {
            it.copy(
                description = description
            )
        }
    }

    fun changeLocation(location: String) {
        _uiState.update {
            it.copy(
                location = location
            )
        }
    }

    fun addProductField() {
        _uiState.update {
            it.copy(
                products = it.products + ""
            )
        }
    }

    fun changeProduct(product: String, index: Int) {
        _uiState.update {
            it.copy(
                products = it.products.toMutableList().apply {
                    this[index] = product
                }
            )
        }
    }

    fun removeProductField(index: Int) {
        _uiState.update {
            it.copy(
                products = it.products.toMutableList().apply {
                    this.removeAt(index)
                }
            )
        }
    }

    fun addBusiness() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }

        viewModelScope.launch {
            try {
                val businessAdditionRequestBody = BusinessAdditionRequestBody(
                    name = uiState.value.name,
                    description = uiState.value.description,
                    location = uiState.value.location,
                    products = uiState.value.products
                )

                val response = apiRepository.addBusiness(
                    token = uiState.value.userDetails.token!!,
                    businessAdditionRequestBody = businessAdditionRequestBody
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            businessId = response.body()?.data?.id!!,
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAIL
                        )
                    }
                    Log.e("businessAdditionResponse_Err", response.toString())
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }
                Log.e("businessAdditionException_Err", e.toString())
            }
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

    private fun getUserDetails() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.getUsers().collect { users ->
                    val user = if(users.isNotEmpty()) users[0] else UserDetails()
                    _uiState.update {
                        it.copy(
                            userDetails = user,
                            userContactData = UserContactData(user.userId, user.username ?: "", user.phoneNumber ?: "", user.email ?: "")
                        )
                    }
                }
            }
        }
    }

    fun enableButton() {
        _uiState.update {
            it.copy(
                buttonEnabled = it.name.isNotEmpty() && it.description.isNotEmpty() && it.products.isNotEmpty() && it.products[0].isNotEmpty()
            )
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.INITIAL
            )
        }
    }

    init {
        getUserRole()
        getUserDetails()
    }
}