package com.escrow.wazipay.ui.screens.users.common.invoice.invoicesList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.repository.DBRepository
import com.escrow.wazipay.ui.screens.users.common.invoice.InvoiceStatus
import com.escrow.wazipay.ui.screens.users.common.invoice.LoadInvoicesStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InvoicesViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(InvoicesUiData())
    val uiState: StateFlow<InvoicesUiData> = _uiState.asStateFlow()

    private val status: String? = savedStateHandle[InvoicesScreenDestination.status]

    fun onChangeStatus(status: String) {
        _uiState.update {
            it.copy(
                selectedStatus = status
            )
        }
        when(status) {
            "All" -> filterInvoices(null)
            "Pending" -> filterInvoices(InvoiceStatus.PENDING)
            "Complete" -> filterInvoices(InvoiceStatus.ACCEPTED)
            "Rejected" -> filterInvoices(InvoiceStatus.REJECTED)
        }
    }

    private fun filterInvoices(invoiceStatus: InvoiceStatus?) {
        _uiState.update {
            it.copy(
                invoiceStatus = invoiceStatus
            )
        }
        getInvoices()
    }

    fun getInvoices() {
        _uiState.update {
            it.copy(
                loadInvoicesStatus = LoadInvoicesStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
               val response = apiRepository.getInvoices(
                   token = uiState.value.userDetails.token!!,
                   query = null,
                   businessId = null,
                   buyerId = if(uiState.value.userRole.role == Role.BUYER) uiState.value.userDetails.userId else null,
                   merchantId = if(uiState.value.userRole.role == Role.MERCHANT) uiState.value.userDetails.userId else null,
                   status = if(uiState.value.invoiceStatus != null) uiState.value.invoiceStatus!!.name else null,
                   startDate = null,
                   endDate = null
               )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            invoices = response.body()?.data!!,
                            loadInvoicesStatus = LoadInvoicesStatus.SUCCESS
                        )
                    }
                } else {
                    if(response.code() == 401) {
                        _uiState.update {
                            it.copy(
                                userAuthorized = false
                            )
                        }
                    }
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

    private fun getInvoicesScreenStartupData() {
        viewModelScope.launch {
            while (uiState.value.userDetails.userId == 0) {
                delay(1000)
            }
            if(status != null) {
                onChangeStatus(status)
            } else {
                getInvoices()
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
                            userRole = userRole!!
                        )
                    }
                }
            }
        }
    }

    init {
        getUserDetails()
        getUserRole()
        getInvoicesScreenStartupData()
    }
}