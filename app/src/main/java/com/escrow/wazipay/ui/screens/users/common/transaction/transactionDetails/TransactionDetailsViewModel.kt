package com.escrow.wazipay.ui.screens.users.common.transaction.transactionDetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.data.room.repository.DBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionDetailsViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(TransactionDetailsUiData())
    val uiState: MutableStateFlow<TransactionDetailsUiData> = _uiState

    private val transactionId: String? = savedStateHandle[TransactionDetailsScreenDestination.transactionId]

    private fun getTransaction() {
        viewModelScope.launch {
            try {
                val response = apiRepository.getTransaction(
                    token = uiState.value.userDetails.token!!,
                    transactionId = transactionId!!.toInt()
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            loadTransactionStatus = LoadTransactionStatus.SUCCESS,
                            transactionData = response.body()?.data!!
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadTransactionStatus = LoadTransactionStatus.FAIL
                        )
                    }
                    Log.e("loadTransaction", "response: $response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadTransactionStatus = LoadTransactionStatus.FAIL
                    )
                }
                Log.e("loadTransaction", "exception: $e")
            }
        }
    }

    private fun loadUserDetails() {
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

    fun loadTransactionDetailsScreenUiData() {
        viewModelScope.launch {
            while (uiState.value.userDetails.userId == 0) {
                delay(2000)
            }
            getTransaction()
        }
    }

    init {
        loadUserDetails()
        loadTransactionDetailsScreenUiData()
    }
}