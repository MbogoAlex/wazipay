package com.escrow.wazipay.ui.general.invoice

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

class InvoicesViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(InvoicesUiData())
    val uiState: StateFlow<InvoicesUiData> = _uiState.asStateFlow()

    fun onChangeStatus(status: String) {
        _uiState.update {
            it.copy(
                selectedStatus = status
            )
        }
        when(status) {
            "All" -> filterInvoices(null)
            "Pending" -> filterInvoices(InvoiceStatus.PENDING)
            "Accepted" -> filterInvoices(InvoiceStatus.ACCEPTED)
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

    private fun getInvoices() {
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
                   buyerId = uiState.value.userDetails.userId,
                   merchantId = null,
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
            getInvoices()
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