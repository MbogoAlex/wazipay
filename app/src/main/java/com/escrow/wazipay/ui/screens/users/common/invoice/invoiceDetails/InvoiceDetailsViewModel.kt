package com.escrow.wazipay.ui.screens.users.common.invoice.invoiceDetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.models.invoice.InvoicePaymentRequestBody
import com.escrow.wazipay.data.network.models.invoice.InvoiceStatusChangeRequestBody
import com.escrow.wazipay.data.network.models.transaction.TransactionMethod
import com.escrow.wazipay.data.network.models.transaction.TransactionStatusRequestBody
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.repository.DBRepository
import com.escrow.wazipay.ui.screens.users.common.invoice.InvoiceStatus
import com.escrow.wazipay.ui.screens.users.common.invoice.InvoiceUpdateStatus
import com.escrow.wazipay.ui.screens.users.common.invoice.LoadInvoicesStatus
import com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment.InvoiceCreationStatus
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
                loadingStatus = LoadingStatus.LOADING
            )
        }
        var transactionPending = true
        viewModelScope.launch {
            try {

                val invoicePaymentRequestBody = InvoicePaymentRequestBody(
                    invoiceId = invoiceId!!.toInt(),
                    transactionMethod = TransactionMethod.valueOf(uiState.value.paymentMethod.name),
                    phoneNumber = uiState.value.phoneNumber,
                    )

                val response = apiRepository.payInvoice(
                    token = uiState.value.userDetails.token!!,
                    invoicePaymentRequestBody = invoicePaymentRequestBody
                )


                if(response.isSuccessful) {

                    if(uiState.value.paymentMethod == PaymentMethod.MPESA) {
                        val transactionStatusRequestBody = TransactionStatusRequestBody(
                            referenceId = response.body()?.data?.partnerReferenceID!!,
                            transactionId = response.body()?.data?.transactionID ?: "",
                            token = response.body()?.data?.transactionToken!!
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
                                    newOrderId = response.body()?.data?.transactionDetails?.orderId!!,
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
                    Log.e("invoicePaymentResponse_Err", response.toString())
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }
                Log.e("invoicePaymentException_Err", e.toString())

            }
        }
    }

    fun getInvoiceDetails() {
        _uiState.update {
            it.copy(
                loadInvoicesStatus = LoadInvoicesStatus.LOADING
            )
        }
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
                    } else {
                        getUserWallet()
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadInvoicesStatus = LoadInvoicesStatus.FAIL
                        )
                    }
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadInvoicesStatus = LoadInvoicesStatus.FAIL
                    )
                }
            }
        }
    }

    fun getUserWallet() {
        Log.d("loadWallet", "LOADING")
        viewModelScope.launch {
            try {
                while(uiState.value.userDetails.userId == 0) {
                    delay(1000)
                }
                val response = apiRepository.getUserWallet(
                    token = uiState.value.userDetails.token!!,
                )

                if(response.isSuccessful) {
                    Log.d("loadWallet", response.body()?.data!!.toString())
                    _uiState.update {
                        it.copy(
                            loadInvoicesStatus = LoadInvoicesStatus.SUCCESS,
                            userWalletData = response.body()?.data!!,
                        )
                    }
                } else {
                    Log.e("loadWallet", "response: $response")
                }

            } catch (e: Exception) {
                Log.e("loadWallet", "exception: $e")
            }
        }
    }

    fun changeInvoiceStatus() {
        _uiState.update {
            it.copy(
                invoiceUpdateStatus = InvoiceUpdateStatus.LOADING
            )
        }

        viewModelScope.launch {
            try {
                val invoiceStatusChangeRequestBody = InvoiceStatusChangeRequestBody(
                    invoiceStatus = InvoiceStatus.REJECTED.name,
                    invoiceId = uiState.value.invoiceData.id
                )

                val response = apiRepository.changeInvoiceState(
                    token = uiState.value.userDetails.token!!,
                    invoiceStatusChangeRequestBody = invoiceStatusChangeRequestBody
                )

                if(response.isSuccessful) {
                    getInvoiceDetails()
                    _uiState.update {
                        it.copy(
                            invoiceUpdateStatus = InvoiceUpdateStatus.SUCCESS
                        )
                    }
                    Log.d("invoiceStatusChange", "response: $response")
                } else {
                    _uiState.update {
                        it.copy(
                            invoiceUpdateStatus = InvoiceUpdateStatus.FAIL
                        )
                    }
                    Log.e("invoiceStatusChange", "response: $response")
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        invoiceUpdateStatus = InvoiceUpdateStatus.FAIL
                    )
                }
                Log.e("invoiceStatusChange", "exception: $e")
            }
        }
    }

    private fun getOrder() {
        Log.d("gettingOrder", "LOADING")
        viewModelScope.launch {
            try {
                val response = apiRepository.getOrder(
                    token = uiState.value.userDetails.token!!,
                    orderId = uiState.value.invoiceData.orderId!!
                )

                if(response.isSuccessful) {
                    Log.d("gettingOrder", response.body()?.data!!.toString())
                    getUserWallet()
                    Log.d("gettingOrder", "FETCH_WALLET_CALLED")
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
                    Log.e("gettingOrder", "response: $response")
                }

            } catch (e: Exception) {
                Log.e("gettingOrder", "exception: $e")

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

    fun getInvoiceScreenData() {
        viewModelScope.launch {
            while (uiState.value.userDetails.userId == 0) {
                delay(1000)
            }
            getInvoiceDetails()
//            getUserWallet()
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.INITIAL,
                invoiceUpdateStatus = InvoiceUpdateStatus.INITIAL
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
        enableButton()
    }
}