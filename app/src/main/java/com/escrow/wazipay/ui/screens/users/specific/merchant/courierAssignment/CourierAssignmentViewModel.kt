package com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.models.courier.CourierAssignmentRequestBody
import com.escrow.wazipay.data.network.models.courier.CourierPaymentRequest
import com.escrow.wazipay.data.network.models.transaction.TransactionStatusRequestBody
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.data.room.repository.DBRepository
import com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment.InvoiceCreationStatus
import com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment.PaymentMethod
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

        var transactionPending = true

        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {

                val courierPaymentRequest = CourierPaymentRequest(
                    orderId = uiState.value.orderData.id,
                    courierId = uiState.value.courier.userId,
                    deliveryCost = uiState.value.amount.toDouble(),
                    transactionMethod = uiState.value.paymentMethod.name,
                    phoneNumber = uiState.value.phoneNumber
                )

                val courierAssignmentRequestBody = CourierAssignmentRequestBody(
                    orderId = uiState.value.orderData.id,
                    courierId = uiState.value.courier.userId,
                    deliveryCost = uiState.value.amount.toDouble(),
                    courierPaymentRequest = courierPaymentRequest
                )

                val response = apiRepository.assignCourier(
                    token = uiState.value.userDetails.token!!,
                    courierAssignmentRequestBody = courierAssignmentRequestBody
                )

                if(response.isSuccessful) {

                    if(uiState.value.paymentMethod == PaymentMethod.MPESA) {
                        val transactionStatusRequestBody = TransactionStatusRequestBody(
                            referenceId = response.body()?.data?.paymentDetails?.partnerReferenceID!!,
                            transactionId = response.body()?.data?.paymentDetails?.transactionID ?: "",
                            token = response.body()?.data?.paymentDetails?.transactionToken!!
                        )

                        while (transactionPending) {
                            delay(2000)

                            val transactionStatusResponse = apiRepository.getTransactionStatus(
                                token = uiState.value.userDetails.token!!,
                                transactionStatusRequestBody = transactionStatusRequestBody
                            )

                            _uiState.update {
                                it.copy(
                                    paymentStage = transactionStatusResponse.body()?.data?.status ?: "",
                                    paymentMessage = transactionStatusResponse.body()?.data?.sovNarration ?: ""
                                )
                            }

                            transactionPending  = transactionStatusResponse.body()?.data?.status == "PENDING"

                        }

                        if(_uiState.value.paymentStage == "SUCCESSFUL") {
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
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                loadingStatus = LoadingStatus.SUCCESS
                            )
                        }
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
                buttonEnabled = if(uiState.value.paymentMethod == PaymentMethod.WAZIPAY_ESCROW) uiState.value.amount.isNotEmpty() else uiState.value.phoneNumber.isNotEmpty() && uiState.value.amount.isNotEmpty()
            )
        }
    }

    init {
        getUser()
        getUserRole()
        loadStartUpData()
    }
}