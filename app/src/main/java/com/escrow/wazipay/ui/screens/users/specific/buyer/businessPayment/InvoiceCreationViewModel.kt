package com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.models.invoice.InvoiceCreationRequestBody
import com.escrow.wazipay.data.network.models.invoice.InvoicePaymentRequestBody
import com.escrow.wazipay.data.network.models.transaction.TransactionMethod
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.data.room.repository.DBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InvoiceCreationViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(InvoiceCreationUiData())
    val uiState: StateFlow<InvoiceCreationUiData> = _uiState.asStateFlow()

    private val businessId: String? = savedStateHandle[InvoiceCreationScreenDestination.businessId]

    fun changePhone(phone: String) {
        _uiState.update {
            it.copy(
                phoneNumber = phone
            )
        }
    }

    fun changeTitle(title: String) {
        _uiState.update {
            it.copy(
                title = title
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

    fun changeAmount(amount: String) {
        _uiState.update {
            it.copy(
                amount = amount
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

    fun createInvoice() {
        _uiState.update {
            it.copy(
                invoiceCreationStatus = InvoiceCreationStatus.LOADING
            )
        }

        viewModelScope.launch {
            try {
               val invoiceCreationRequestBody = InvoiceCreationRequestBody(
                   title = uiState.value.title,
                   description = uiState.value.description,
                   amount = uiState.value.amount.toDouble(),
                   businessId = businessId!!.toInt(),
                   buyerId = uiState.value.userDetails.userId
               )

               val response = apiRepository.createInvoice(
                   token = uiState.value.userDetails.token!!,
                   invoiceCreationRequestBody = invoiceCreationRequestBody
               )

               if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            invoiceId = response.body()?.data?.id!!.toString()
                        )
                    }

                   payInvoice()
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
                           invoiceCreationStatus = InvoiceCreationStatus.FAIL
                       )
                   }
                   Log.e("invoiceCreationResponse_Err", response.toString())
               }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        invoiceCreationStatus = InvoiceCreationStatus.FAIL
                    )
                }
                Log.e("invoiceCreationException_Err", e.toString())
            }

        }
    }

    private fun payInvoice() {
        var transactionComplete = false

        viewModelScope.launch {
            try {

                val invoicePaymentRequestBody = InvoicePaymentRequestBody(
                    invoiceId = uiState.value.invoiceId.toInt(),
                    transactionMethod = TransactionMethod.valueOf(uiState.value.paymentMethod.name),
                    phoneNumber = uiState.value.phoneNumber
                )

               val response = apiRepository.payInvoice(
                   token = uiState.value.userDetails.token!!,
                   invoicePaymentRequestBody = invoicePaymentRequestBody
               )

               if(response.isSuccessful) {

                   while (!transactionComplete) {
                       delay(2000)

                       transactionComplete = apiRepository.getTransactionStatus(
                           token = uiState.value.userDetails.token!!,
                           transactionCode = response.body()?.data?.transactionCode!!
                       ).body()?.data?.status!!
                   }

                   _uiState.update {
                       it.copy(
                           orderId = response.body()?.data?.orderId!!.toString(),
                           invoiceCreationStatus = InvoiceCreationStatus.SUCCESS
                       )
                   }

               } else {
                   _uiState.update {
                       it.copy(
                           invoiceCreationStatus = InvoiceCreationStatus.FAIL
                       )
                   }
                   Log.e("invoicePaymentResponse_Err", response.toString())
               }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        invoiceCreationStatus = InvoiceCreationStatus.FAIL
                    )
                }
                Log.e("invoicePaymentException_Err", e.toString())

            }
        }
    }

    private fun getBusiness() {
        viewModelScope.launch {
            try {
                val response = apiRepository.getBusiness(
                    token = uiState.value.userDetails.token!!,
                    businessId = businessId!!.toInt()
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

    private fun getUserWallet() {
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
                } else {
                    if(response.code() == 401) {
                        _uiState.update {
                            it.copy(
                                unauthorized = true
                            )
                        }
                    }

                }

            } catch (e: Exception) {


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
                            userDetails = if(users.isNotEmpty()) users[0] else UserDetails()
                        )
                    }
                }
            }
        }
    }

    private fun getInvoiceCreationScreenStartupUiData() {
        viewModelScope.launch {
            while (uiState.value.userDetails.userId == 0) {
                delay(1000)
            }
            getBusiness()
            getUserWallet()
        }
    }

    fun enableButton() {
        _uiState.update {
            it.copy(
                buttonEnabled = if(uiState.value.paymentMethod == PaymentMethod.MPESA) uiState.value.title.isNotEmpty() &&
                uiState.value.description.isNotEmpty() &&
                uiState.value.amount.isNotEmpty() &&
                uiState.value.phoneNumber.isNotEmpty()
                else uiState.value.title.isNotEmpty() &&
                        uiState.value.description.isNotEmpty() &&
                        uiState.value.amount.isNotEmpty()
            )
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                invoiceCreationStatus = InvoiceCreationStatus.INITIAL
            )
        }
    }

    init {
        getUserDetails()
        getUserRole()
        getInvoiceCreationScreenStartupUiData()
    }

}