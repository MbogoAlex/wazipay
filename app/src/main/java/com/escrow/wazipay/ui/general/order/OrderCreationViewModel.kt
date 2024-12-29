package com.escrow.wazipay.ui.general.order

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.models.order.OrderCreationRequestBody
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.repository.DBRepository
import com.escrow.wazipay.ui.general.business.LoadBusinessStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderCreationViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(OrderCreationUiData())
    val uiState: StateFlow<OrderCreationUiData> = _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.update {
            it.copy(
                productName = name
            )
        }
    }

    fun updateDescription(description: String) {
        _uiState.update {
            it.copy(
                description = description
            )
        }
    }

    fun updateAmount(amount: String) {
        _uiState.update {
            it.copy(
                amount = amount
            )
        }
    }

    fun createOrder() {
        _uiState.update {
            it.copy(
                orderCreationStatus = OrderCreationStatus.LOADING
            )
        }

        viewModelScope.launch {
            val orderCreationRequestBody = OrderCreationRequestBody(
                businessId = uiState.value.businessData.id,
                name = uiState.value.productName,
                description = uiState.value.description,
                amount = uiState.value.amount.toDouble()
            )

            try {
                val response = apiRepository.createOrder(
                    token = uiState.value.userDetails.token!!,
                    orderCreationRequestBody = orderCreationRequestBody
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            orderCreationStatus = OrderCreationStatus.SUCCESS
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
                            orderCreationStatus = OrderCreationStatus.FAIL
                        )
                    }
                    Log.e("orderCreationResponse_err", response.toString())
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        orderCreationStatus = OrderCreationStatus.FAIL
                    )
                }
                Log.e("orderCreationException_err", e.toString())

            }
        }

    }

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
                    Log.e("loadBusinessResponse_err", response.toString())
                }
            } catch (e: Exception) {
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

    private fun getOrderCreationScreenData() {
        viewModelScope.launch {
            while (uiState.value.userDetails.userId == 0) {
                delay(1000)
            }
            getBusiness()
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                orderCreationStatus = OrderCreationStatus.INITIAL
            )
        }
    }

    fun enableButton() {
        _uiState.update {
            it.copy(
                buttonEnabled = uiState.value.productName.isNotEmpty() &&
                uiState.value.description.isNotEmpty() &&
                uiState.value.amount.isNotEmpty()
            )
        }
    }

    init {
        _uiState.update {
            it.copy(
                businessId = savedStateHandle[OrderCreationScreenDestination.businessId]
            )
        }
        getUserDetails()
        getBusiness()
    }
}