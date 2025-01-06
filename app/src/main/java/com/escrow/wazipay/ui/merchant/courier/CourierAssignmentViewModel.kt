package com.escrow.wazipay.ui.merchant.courier

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.models.courier.CourierAssignmentRequestBody
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.data.room.repository.DBRepository
import com.escrow.wazipay.ui.buyer.invoice.PaymentMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CourierAssignmentViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(CourierAssignmentUiData())
    val uiState: StateFlow<CourierAssignmentUiData> = _uiState.asStateFlow()

    private val courierId: String? = savedStateHandle[CourierAssignmentScreenDestination.courierId]
    private val orderId: String? = savedStateHandle[CourierAssignmentScreenDestination.orderId]

    fun changeAmount(amount: String) {
        _uiState.update {
            it.copy(
                amount = amount
            )
        }
    }

    fun changePhone(phone: String) {
        _uiState.update {
            it.copy(
                phoneNumber = phone
            )
        }
    }

    fun changePaymentMethod(paymentMethod: PaymentMethod) {
        _uiState.update {
            it.copy(
                paymentMethod = paymentMethod
            )
        }
    }

    fun assignCourier() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {

                val courierAssignmentRequestBody = CourierAssignmentRequestBody(
                    orderId = uiState.value.orderData.id,
                    courierId = uiState.value.courier.userId,
                    deliveryCost = uiState.value.amount.toDouble()
                )

                val response = apiRepository.assignCourier(
                    token = uiState.value.userDetails.token!!,
                    courierAssignmentRequestBody = courierAssignmentRequestBody
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAIL
                        )
                    }
                    Log.e("courierAssignmentResponse_err", response.toString())
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }
                Log.e("courierAssignmentException_err", e.toString())

            }
        }
    }

    private fun getCourier() {
        viewModelScope.launch {
            try {
                val response = apiRepository.getUserDetails(
                    token = uiState.value.userDetails.token!!,
                    userId = courierId!!.toInt()
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            courier = response.body()?.data!!
                        )
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun getOrder() {
        viewModelScope.launch {
            try {
                val response = apiRepository.getOrder(
                    token = uiState.value.userDetails.token!!,
                    orderId = orderId!!.toInt()
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            orderData = response.body()?.data!!
                        )
                    }
                }
            } catch (e: Exception) {

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

    private fun loadStartUpData() {
        viewModelScope.launch {
            while(uiState.value.userDetails.userId == 0) {
                delay(1000)
            }
            getCourier()
            getOrder()
            getUserWallet()
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

    fun getUserWallet() {
        viewModelScope.launch {
            try {
                while(uiState.value.userDetails.userId == 0) {
                    delay(1000)
                }
                val response = apiRepository.getUserWallet(
                    token = uiState.value.userDetails.token!!,
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            userWalletData = response.body()?.data!!,
                        )
                    }
                }

            } catch (e: Exception) {
                Log.e("getUserException_err", e.toString())
            }
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.INITIAL
            )
        }
    }

    fun enableButton() {
        _uiState.update {
            it.copy(
                buttonEnabled = if(uiState.value.paymentMethod == PaymentMethod.WAZIPAY) uiState.value.amount.isNotEmpty() else uiState.value.phoneNumber.isNotEmpty() && uiState.value.amount.isNotEmpty()
            )
        }
    }

    init {
        getUser()
        getUserRole()
        loadStartUpData()
    }
}