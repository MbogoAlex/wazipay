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

class OrdersViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
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
                    businessId = if(uiState.value.businessId != null) uiState.value.businessId!!.toInt() else null,
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

    fun changeOrderStage(orderStage: String) {
        _uiState.update {
            it.copy(
                selectedStage = orderStage
            )
        }
        when(orderStage) {
            "All" -> filterOrders(OrderStage.All)
            "Completed" -> filterOrders(OrderStage.COMPLETE)
            "In Transit" -> filterOrders(OrderStage.IN_TRANSIT)
            "Pending pickup" -> filterOrders(OrderStage.PENDING_PICKUP)
            "Cancelled" -> filterOrders(OrderStage.CANCELLED)
            "Refunded" -> filterOrders(OrderStage.REFUNDED)
        }
    }

    private fun filterOrders(orderStage: OrderStage) {
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
                loadOrdersStatus = LoadOrdersStatus.INITIAL
            )
        }
    }

    init {
        _uiState.update {
            it.copy(
                businessId = savedStateHandle[OrdersScreenDestination.businessId]
            )
        }
        getUserDetails()
        getUserRole()
        getOrdersScreenData()
    }
}