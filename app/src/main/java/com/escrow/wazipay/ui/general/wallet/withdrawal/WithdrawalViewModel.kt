package com.escrow.wazipay.ui.general.wallet.withdrawal

import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.models.wallet.WithdrawalRequestBody
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

class WithdrawalViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiState = MutableStateFlow(WithdrawalUiData())
    val uiState: StateFlow<WithdrawalUiData> = _uiState.asStateFlow()

    fun updatePhoneNumber(phone: String) {
        _uiState.update {
            it.copy(
                phoneNumber = phone.ifEmpty { uiState.value.userDetails.phoneNumber ?: "" }
            )
        }
    }

    fun updateWithdrawalAmount(amount: String) {
        _uiState.update {
            it.copy(
                withdrawalAmount = amount
            )
        }
    }

    fun withdraw() {
        _uiState.update {
            it.copy(
                withdrawalStatus = WithdrawalStatus.LOADING
            )
        }

        viewModelScope.launch {
            try {

                val withdrawalRequestBody = WithdrawalRequestBody(
                    amount = uiState.value.withdrawalAmount.toDouble()
                )

                val response = apiRepository.withdraw(
                    token = uiState.value.userDetails.token!!,
                    withdrawalRequestBody = withdrawalRequestBody
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            withdrawMessage = "Withdrawal successful",
                            newBalance = response.body()?.data?.balance!!,
                            userWalletData = response.body()?.data!!,
                            withdrawalStatus = WithdrawalStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            withdrawMessage = "Withdrawal failed.",
                            withdrawalStatus = WithdrawalStatus.FAIL
                        )
                    }
                    Log.e("withdrawalResponse_err", response.toString())
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        withdrawMessage = "Withdrawal failed.",
                        withdrawalStatus = WithdrawalStatus.FAIL
                    )
                }
                Log.e("withdrawalException_err", e.toString())
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
                            userDetails = if(users.isEmpty()) UserDetails() else users[0],
                            phoneNumber = uiState.value.phoneNumber.ifEmpty { users[0].phoneNumber ?: "" }
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
                buttonEnabled = uiState.value.withdrawalAmount.isNotEmpty() &&
                        uiState.value.withdrawalAmount != "0" &&
                uiState.value.phoneNumber.length == 10
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
                withdrawalStatus = WithdrawalStatus.INITIAL
            )
        }
    }

    init {
        getUserRole()
        getUser()
        loadStartUpData()

    }
}