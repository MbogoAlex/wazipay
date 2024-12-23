package com.escrow.wazipay.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.models.common.SetPinRequestBody
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.data.room.repository.DBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PinViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(PinUiData())
    val uiState: StateFlow<PinUiData> = _uiState.asStateFlow()


    fun updatePin(pin: String) {
        _uiState.update {
            it.copy(
                pin = pin
            )
        }
    }

    fun setPin() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    pinSetStatus = PinSetStatus.LOADING
                )
            }
            val setPinRequestBody = SetPinRequestBody(
                userId = uiState.value.userDetails.userId,
                pin = uiState.value.pin
            )

            Log.d("setPin", setPinRequestBody.toString())

            try {
                val response = apiRepository.setUserPin(setPinRequestBody = setPinRequestBody)
                if(response.isSuccessful) {
                    withContext(Dispatchers.IO) {
                        dbRepository.updateUser(
                            userDetails = uiState.value.userDetails.copy(
                                pin = uiState.value.pin
                            )
                        )
                    }
                    var user = dbRepository.getUser(userId = uiState.value.userDetails.userId).first()

                    while(user.pin == null) {
                        delay(1000)
                        user = dbRepository.getUser(userId = uiState.value.userDetails.userId).first()
                    }

                    _uiState.update {
                        it.copy(
                            pinSetMessage = "Pin set successfully",
                            pinSetStatus = PinSetStatus.SUCCESS
                        )
                    }
                } else {
                    Log.e("pin_set_response_err", response.toString())
                    _uiState.update {
                        it.copy(
                            pinSetMessage = response.message(),
                            pinSetStatus = PinSetStatus.FAIL
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("pin_set_exception_err", e.toString())
                _uiState.update {
                    it.copy(
                        pinSetMessage = e.message.toString(),
                        pinSetStatus = PinSetStatus.FAIL
                    )
                }
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.getUsers().collect() {users ->
                    _uiState.update {
                        it.copy(
                            userDetails = users[0]
                        )
                    }
                }
            }
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                pinSetStatus = PinSetStatus.INITIAL
            )
        }
    }

    fun enableButton() {
        _uiState.update {
            it.copy(
                buttonEnabled = uiState.value.pin.length == 6
            )
        }
    }

    init {
        getUser()
    }
}