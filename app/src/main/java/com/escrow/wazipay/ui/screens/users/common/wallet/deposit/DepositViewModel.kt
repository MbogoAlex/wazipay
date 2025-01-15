package com.escrow.wazipay.ui.screens.users.common.wallet.deposit

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.models.wallet.DepositRequestBody
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

class DepositViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(DepositUiData())
    val uiState: StateFlow<DepositUiData> = _uiState.asStateFlow()

    fun onChangePhoneNumber(phone: String) {
        _uiState.update {
            it.copy(
                phoneNumber = phone
            )
        }
    }

    fun updateAmount(amount: String) {
        _uiState.update {
            it.copy(
                amount = amount
            )
        }
    }

    fun deposit() {
        _uiState.update {
            it.copy(
                depositStatus = DepositStatus.LOADING
            )
        }

        viewModelScope.launch {
            try {

               val depositRequestBody = DepositRequestBody(
                   phoneNumber = uiState.value.phoneNumber,
                   amount = uiState.value.amount.toDouble()
               )

               val response = apiRepository.deposit(
                   token = uiState.value.userDetails.token!!,
                   depositRequestBody = depositRequestBody
               )

               if(response.isSuccessful) {
                   val transactionCode = apiRepository.getTransactions(
                       token = uiState.value.userDetails.token!!,
                       userId = uiState.value.userDetails.userId,
                       query = null,
                       transactionCode = null,
                       transactionType = "WALLET_DEPOSIT",
                       startDate = null,
                       endDate = null
                   ).body()?.data!!
                       .sortedBy { it.createdAt }
                       .first().transactionCode

                   Log.d("transactionCode", transactionCode)

                   var paymentComplete = false

                   while(!paymentComplete) {
                       delay(5000)
                       val paymentCompleteResponse = apiRepository.getTransactionStatus(
                           token = uiState.value.userDetails.token!!,
                           transactionCode = transactionCode
                       )
                       Log.d("paymentCompleteResponse", paymentCompleteResponse.toString())

                       paymentComplete = paymentCompleteResponse.body()?.data?.status!!
                   }

                   _uiState.update {
                       it.copy(
                           depositMessage = "Deposit successful",
                           newBalance = response.body()?.data?.balance!!,
                           userWalletData = response.body()?.data!!,
                           depositStatus = DepositStatus.SUCCESS
                       )
                   }
               } else {
                   _uiState.update {
                       it.copy(
                           depositMessage = "Deposit failed.",
                           depositStatus = DepositStatus.FAIL
                       )
                   }
                   Log.e("depositResponse_err", response.toString())
               }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        depositMessage = "Deposit failed.",
                        depositStatus = DepositStatus.FAIL
                    )
                }
                Log.e("depositException_err", e.toString())
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

    private fun getUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.getUsers().collect {users ->
                    _uiState.update {
                        it.copy(
                            userDetails = if(users.isEmpty()) UserDetails() else users[0]
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
            getUserWallet()
        }
    }

    fun enableButton() {
        _uiState.update {
            it.copy(
                buttonEnabled = uiState.value.amount.isNotEmpty() && uiState.value.amount != "0"
            )
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

    fun resetStatus() {
        _uiState.update {
            it.copy(
                depositStatus = DepositStatus.INITIAL
            )
        }
    }

    init {
        getUserRole()
        getUser()
        loadStartUpData()
    }
}