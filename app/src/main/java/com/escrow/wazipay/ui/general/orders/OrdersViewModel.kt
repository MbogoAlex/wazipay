package com.escrow.wazipay.ui.general.orders

import android.util.Log
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

class OrdersViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(OrdersUiData())
    val uiState: StateFlow<OrdersUiData> = _uiState.asStateFlow()

    private fun getOrders() {

        _uiState.update {
            it.copy(
                loadOrdersStatus = LoadOrdersStatus.INITIAL
            )
        }

        Log.d("fetchingOrders", uiState.value.userDetails.toString())
        viewModelScope.launch {
            try {
                val response = apiRepository.getOrders(
                    token = uiState.value.userDetails.token!!,
                    query = null,
                    code = null,
                    merchantId = null,
                    buyerId = uiState.value.userDetails.userId,
                    courierId = null,
                    stage = if(uiState.value.orderStage == OrderStage.All) null else uiState.value.orderStage.name,
                    startDate = null,
                    endDate = null
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            orders = response.body()?.data!!,
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

                    Log.e("getOrdersResponse_err", response.toString())
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadOrdersStatus = LoadOrdersStatus.FAIL
                    )
                }

                Log.e("getOrdersException_err", e.toString())
            }
        }
    }

    fun changeOrderStage(orderStage: OrderStage) {
        _uiState.update {
            it.copy(
                orderStage = orderStage
            )
        }
        getOrders()
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

    private fun getOrdersScreenData() {
        viewModelScope.launch {
            while (uiState.value.userDetails.userId == 0) {
                delay(1000)
            }

            getOrders()
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                loadOrdersStatus = LoadOrdersStatus.INITIAL
            )
        }
    }

    init {
        getUserDetails()
        getOrdersScreenData()
    }
}