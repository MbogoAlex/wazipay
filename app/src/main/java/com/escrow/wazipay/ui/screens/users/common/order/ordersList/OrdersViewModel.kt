package com.escrow.wazipay.ui.screens.users.common.order.ordersList

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.data.room.repository.DBRepository
import com.escrow.wazipay.ui.screens.users.common.order.LoadOrdersStatus
import com.escrow.wazipay.ui.screens.users.common.order.OrderStage
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


    fun getOrders() {

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
                    merchantId = if(uiState.value.role == Role.MERCHANT) uiState.value.userDetails.userId else null,
                    buyerId = if(uiState.value.role == Role.BUYER) uiState.value.userDetails.userId else null,
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
                            userDetails = if(users.isNotEmpty()) users[0] else UserDetails()
                        )
                    }
                }
            }
        }
    }

    fun getOrdersScreenData() {
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
        val stage: String? = savedStateHandle[OrdersScreenDestination.stage]
        _uiState.update {
            it.copy(
                businessId = savedStateHandle[OrdersScreenDestination.businessId],
                orderStage = if(stage != null) OrderStage.valueOf(stage.uppercase()) else OrderStage.All,
                selectedStage = if(stage != null) if(stage == "IN_TRANSIT".lowercase()) "In Transit" else if (stage == "PENDING_PICKUP".lowercase()) "Pending pickup" else stage.replaceFirstChar { first -> first.uppercase() } else "All",
            )
        }
        getUserDetails()
        getUserRole()
        getOrdersScreenData()
    }
}