package com.escrow.wazipay.ui.buyer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.repository.DBRepository
import com.escrow.wazipay.ui.general.order.LoadOrdersStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BuyerDashboardViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow(BuyerDashboardUiData())
    val uiState: StateFlow<BuyerDashboardUiData> = _uiState.asStateFlow()

    private fun getUserDetails() {

        _uiState.update {
            it.copy(
                loadUserStatus = LoadUserStatus.INITIAL
            )
        }

        viewModelScope.launch {
            try {
                while(uiState.value.userDetails.userId == 0) {
                    delay(1000)
                }
                val response = apiRepository.getUserDetails(
                    token = uiState.value.userDetails.token!!,
                    userId = uiState.value.userDetails.userId
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            userDetailsData = response.body()?.data!!,
                            loadUserStatus = LoadUserStatus.SUCCESS
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
                            loadUserStatus = LoadUserStatus.FAIL
                        )
                    }

                    Log.e("getUserResponse_err", response.toString())
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadUserStatus = LoadUserStatus.FAIL
                    )
                }

                Log.e("getUserException_err", e.toString())
            }
        }
    }

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
                   stage = null,
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

    private fun getUserWallet() {
        _uiState.update {
            it.copy(
                loadWalletStatus = LoadWalletStatus.INITIAL
            )
        }

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
                            loadWalletStatus = LoadWalletStatus.SUCCESS
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
                            loadWalletStatus = LoadWalletStatus.FAIL
                        )
                    }

                    Log.e("getUserResponse_err", response.toString())
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadWalletStatus = LoadWalletStatus.FAIL
                    )
                }

                Log.e("getUserException_err", e.toString())
            }
        }
    }

    private fun getInvoices() {

        _uiState.update {
            it.copy(
                loadInvoicesStatus = LoadInvoicesStatus.INITIAL
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
                    status = null,
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
                                unauthorized = true
                            )
                        }
                    }

                    _uiState.update {
                        it.copy(
                            loadInvoicesStatus = LoadInvoicesStatus.FAIL
                        )
                    }

                    Log.e("getInvoicesResponse_err", response.toString())
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadInvoicesStatus = LoadInvoicesStatus.FAIL
                    )
                }

                Log.e("getInvoicesException_err", e.toString())
            }
        }
    }

    private fun getTransactions() {
        _uiState.update {
            it.copy(
                loadTransactionsStatus = LoadTransactionsStatus.INITIAL
            )
        }

        viewModelScope.launch {
            try {
                val response = apiRepository.getTransactions(
                    token = uiState.value.userDetails.token!!,
                    userId = uiState.value.userDetails.userId,
                    query = null,
                    transactionCode = null,
                    transactionType = null,
                    startDate = null,
                    endDate = null
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            transactions = response.body()?.data!!,
                            loadInvoicesStatus = LoadInvoicesStatus.SUCCESS
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
                            loadTransactionsStatus = LoadTransactionsStatus.FAIL
                        )
                    }

                    Log.e("getTransactionsResponse_err", response.toString())
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadTransactionsStatus = LoadTransactionsStatus.FAIL
                    )
                }

                Log.e("getTransactionsException_err", e.toString())
            }
        }
    }

    private fun fetchUserFromRoom() {
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

    private fun loadDashboardData() {
        viewModelScope.launch {
            while (uiState.value.userDetails.userId == 0) {
                delay(1000)
            }

            getUserDetails()
            getOrders()
            getInvoices()
            getTransactions()
            getUserWallet()
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                loadUserStatus = LoadUserStatus.INITIAL,
                loadOrdersStatus = LoadOrdersStatus.INITIAL,
                loadWalletStatus = LoadWalletStatus.INITIAL,
                loadInvoicesStatus = LoadInvoicesStatus.INITIAL,
                loadTransactionsStatus = LoadTransactionsStatus.INITIAL
            )
        }
    }

    init {
        fetchUserFromRoom()
        loadDashboardData()
    }

}