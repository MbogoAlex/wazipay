package com.escrow.wazipay.ui.screens.users.common.wallet.deposit

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.models.transaction.TransactionStatusRequestBody
import com.escrow.wazipay.data.network.models.wallet.DepositRequestBody
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
        var depositPending = true
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

                   val transactionStatusRequestBody = TransactionStatusRequestBody(
                       referenceId = response.body()?.data?.partnerReferenceID!!,
                       transactionId = response.body()?.data?.transactionID ?: "",
                       token = response.body()?.data?.transactionToken!!
                   )

                   while(depositPending) {

                       delay(2000)
                       val transactionStatusResponse = apiRepository.getTransactionStatus(
                           token = uiState.value.userDetails.token!!,
                           transactionStatusRequestBody = transactionStatusRequestBody
                       )

                       _uiState.update {
                           it.copy(
                               depositStage = transactionStatusResponse.body()?.data?.status ?: "",
                               depositMessage = transactionStatusResponse.body()?.data?.sovNarration ?: ""
                           )
                       }

                       depositPending  = transactionStatusResponse.body()?.data?.status == "PENDING"
                   }

                   if(_uiState.value.depositStage == "SUCCESSFUL") {
                       getUserWallet()
                       _uiState.update {
                           it.copy(
                               newBalance = response.body()?.data?.transactionDetails?.balance!!,
                               userWalletData = response.body()?.data?.transactionDetails!!,
                               depositStatus = DepositStatus.SUCCESS
                           )
                       }
                   } else {
                       _uiState.update {
                           it.copy(
                               depositStatus = DepositStatus.FAIL

                           )
                       }
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