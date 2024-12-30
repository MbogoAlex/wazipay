package com.escrow.wazipay.ui.general.transaction

import android.os.Build
import androidx.annotation.RequiresApi
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
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class TransactionsViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(TransactionsUiData())
    val uiState: StateFlow<TransactionsUiData> = _uiState.asStateFlow()

    fun changeSearchText(text: String) {
        _uiState.update {
            it.copy(
                searchText = text
            )
        }

        getTransactions()
    }

    fun changeStartDate(startDate: LocalDate) {
        _uiState.update {
            it.copy(
                startDate = startDate.toString()
            )
        }

        getTransactions()
    }

    fun changeEndDate(endDate: LocalDate) {
        _uiState.update {
            it.copy(
                endDate = endDate.toString()
            )
        }

        getTransactions()
    }

    private fun getTransactions() {

        _uiState.update {
            it.copy(
                loadTransactionsStatus = LoadTransactionsStatus.LOADING
            )
        }

        viewModelScope.launch {
            try {
                val response = apiRepository.getTransactions(
                    token = uiState.value.userDetails.token!!,
                    userId = uiState.value.userDetails.userId,
                    query = uiState.value.searchText,
                    transactionCode = null,
                    transactionType = null,
                    startDate = uiState.value.startDate,
                    endDate = uiState.value.endDate
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            transactions = response.body()?.data!!,
                            loadTransactionsStatus = LoadTransactionsStatus.SUCCESS
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
                            loadTransactionsStatus = LoadTransactionsStatus.FAIL
                        )
                    }
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadTransactionsStatus = LoadTransactionsStatus.FAIL
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeDates() {
        _uiState.update {
            it.copy(
                startDate = LocalDate.now().withDayOfMonth(1).toString(),
                endDate = LocalDate.now().toString()
            )
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

    private fun loadTransactionsScreenUiData() {
        viewModelScope.launch {
            while(uiState.value.userDetails.userId == 0) {
                delay(1000)
            }

            getTransactions()
        }
    }

    init {
        initializeDates()
        getUserDetails()
        loadTransactionsScreenUiData()
    }
}