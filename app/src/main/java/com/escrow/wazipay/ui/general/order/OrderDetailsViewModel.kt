package com.escrow.wazipay.ui.general.order

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.repository.DBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderDetailsViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiState = MutableStateFlow(OrderDetailsUiData())
    val uiState: StateFlow<OrderDetailsUiData> = _uiState.asStateFlow()

    private val orderId: String? = savedStateHandle[OrderDetailsScreenDestination.orderId]

    private fun getOrder() {

        _uiState.update {
            it.copy(
                loadOrdersStatus = LoadOrdersStatus.INITIAL
            )
        }

        viewModelScope.launch {
            try {
                val response = apiRepository.getOrder(
                    token = uiState.value.userDetails.token!!,
                    orderId = orderId!!.toInt()
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            orderData = response.body()?.data!!,
                            loadOrdersStatus = LoadOrdersStatus.SUCCESS
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
                            loadOrdersStatus = LoadOrdersStatus.FAIL
                        )
                    }
                    Log.e("getOrderResponse_err", response.toString())
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadOrdersStatus = LoadOrdersStatus.FAIL
                    )
                }
                Log.e("getOrderException_err", e.toString())

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
                    _uiState.update {
                        it.copy(
                            userDetails = users[0]
                        )
                    }
                }
            }
        }
    }

    private fun loadOrderDetailScreenStartupUiData() {
        viewModelScope.launch {
            while (uiState.value.userDetails.userId == 0) {
                delay(1000)
            }
            getOrder()
        }
    }

    init {
        getUserDetails()
        getUserRole()
        loadOrderDetailScreenStartupUiData()
    }
}