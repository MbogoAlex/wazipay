package com.escrow.wazipay.ui.general.wallet

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
                   amount = uiState.value.amount.toDouble()
               )

               val response = apiRepository.deposit(
                   token = uiState.value.userDetails.token!!,
                   depositRequestBody = depositRequestBody
               )

               if(response.isSuccessful) {
                   _uiState.update {
                       it.copy(
                           depositMessage = "Deposit successful",
                           newBalance = response.body()?.data?.balance!!,
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

    fun resetStatus() {
        _uiState.update {
            it.copy(
                depositStatus = DepositStatus.INITIAL
            )
        }
    }

    init {
        getUser()
        loadStartUpData()

        _uiState.update {
            it.copy(
                profile = savedStateHandle[DepositScreenDestination.profile]
            )
        }
    }
}