package com.escrow.wazipay.ui.screens.users.specific.merchant.invoiceCreation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.models.invoice.InvoiceCreationRequestBody
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.data.room.repository.DBRepository
import com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment.InvoiceCreationStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InvoiceIssuanceViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(InvoiceIssuanceUiData())
    val uiState: StateFlow<InvoiceIssuanceUiData> = _uiState.asStateFlow()

    private val businessId: String? = savedStateHandle[InvoiceIssuanceScreenDestination.businessId]
    private val buyerId: String? = savedStateHandle[InvoiceIssuanceScreenDestination.buyerId]

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

    fun issueInvoice() {
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
                    buyerId = buyerId!!.toInt()
                )

                val response = apiRepository.createInvoice(
                    token = uiState.value.userDetails.token!!,
                    invoiceCreationRequestBody = invoiceCreationRequestBody
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            invoiceId = response.body()?.data?.id!!,
                            invoiceCreationStatus = InvoiceCreationStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            invoiceCreationStatus = InvoiceCreationStatus.FAIL
                        )
                    }

                    Log.e("issueInvoiceRequest_Err", response.toString())
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        invoiceCreationStatus = InvoiceCreationStatus.FAIL
                    )
                }

                Log.e("issueInvoiceException_Err", e.toString())
            }
        }
    }

    private fun getBuyer() {
        viewModelScope.launch {
            try {
               val response = apiRepository.getUser(
                   token = uiState.value.userDetails.token!!,
                   userId = buyerId!!.toInt()
               )

               if(response.isSuccessful) {
                   _uiState.update {
                       it.copy(
                           buyer = response.body()?.data!!
                       )
                   }
                   Log.d("invoiceIssuance", "Buyer: ${uiState.value.buyer}")
               } else {
                   Log.e("invoiceIssuanceBusinessResponse_ERR", response.toString())
               }
            } catch (e: Exception) {
                Log.e("invoiceIssuanceBuyerException_ERR", e.toString())
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
                            businessData = response.body()?.data!!
                        )
                    }
                    Log.d("invoiceIssuance", "Business: ${uiState.value.buyer}")
                } else {
                    Log.e("invoiceIssuanceBusinessResponse_ERR", response.toString())
                }
                Log.d("invoiceIssuance", "Business: ${uiState.value.buyer}")
            } catch (e: Exception) {
                Log.e("invoiceIssuanceBusinessException_ERR", e.toString())
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

    private fun loadInvoiceIssuanceUiData() {
        viewModelScope.launch {
            while (uiState.value.userDetails.userId == 0) {
                delay(1000)
            }
            getBusiness()
            getBuyer()
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                invoiceCreationStatus = InvoiceCreationStatus.INITIAL
            )
        }
    }

    fun enableButton() {
        _uiState.update {
            it.copy(
                buttonEnabled = uiState.value.title.isNotEmpty() &&
                uiState.value.description.isNotEmpty() &&
                uiState.value.amount.isNotEmpty()
            )
        }
    }

    init {
        getUserRole()
        getUserDetails()
        loadInvoiceIssuanceUiData()
    }
}