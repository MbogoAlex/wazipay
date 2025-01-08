package com.escrow.wazipay.ui.screens.users.common.invoice.invoiceDetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.repository.DBRepository
import com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment.PaymentMethod
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.LoadingStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InvoiceDetailsViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _uiState = MutableStateFlow(InvoiceDetailsUiData())
    val uiState: StateFlow<InvoiceDetailsUiData> = _uiState.asStateFlow()

    private val invoiceId: String? = savedStateHandle[InvoiceDetailsScreenDestination.invoiceId]

    fun changePhoneNumber(phone: String) {
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

    fun payInvoice() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.INITIAL
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.payInvoice(
                    token = uiState.value.userDetails.token!!,
                    invoiceId = invoiceId!!.toInt()
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            newOrderId = response.body()?.data?.orderId!!,
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAIL
                        )
                    }
                    Log.e("payInvoiceResponse_err", response.toString())
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }
                Log.e("payInvoiceException_err", e.toString())
            }
        }
    }

    fun getInvoiceDetails() {
        viewModelScope.launch {
            try {
               val response = apiRepository.getInvoice(
                   token = uiState.value.userDetails.token!!,
                   invoiceId = invoiceId!!.toInt()
               )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            invoiceData = response.body()!!.data,
                        )
                    }
                    Log.d("invoiceDetails", uiState.value.invoiceData.toString())
                    if(uiState.value.invoiceData.orderId != null) {
                        getOrder()
                    }
                }

            } catch (e: Exception) {

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

    private fun getOrder() {
        viewModelScope.launch {
            try {
                val response = apiRepository.getOrder(
                    token = uiState.value.userDetails.token!!,
                    orderId = uiState.value.orderData.id
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            orderData = response.body()?.data!!,
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
                    Log.e("getOrderResponse_err", response.toString())
                }

            } catch (e: Exception) {
                Log.e("getOrderException_err", e.toString())

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

    private fun getInvoiceScreenData() {
        viewModelScope.launch {
            while (uiState.value.userDetails.userId == 0) {
                delay(1000)
            }
            getInvoiceDetails()
            getUserWallet()
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
                buttonEnabled = if(uiState.value.paymentMethod == PaymentMethod.MPESA) uiState.value.phoneNumber.isNotEmpty() else true
            )
        }
    }

    init {
        getUserRole()
        getUserDetails()
        getInvoiceScreenData()
    }
}