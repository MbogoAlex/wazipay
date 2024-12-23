package com.escrow.wazipay.ui.auth

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.models.common.LoginRequestBody
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

class LoginViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiData())
    val uiState: StateFlow<LoginUiData> = _uiState.asStateFlow()

    fun updatePhoneNumber(phoneNumber: String) {
        _uiState.update {
            it.copy(
                phoneNumber = phoneNumber
            )
        }
    }

    fun updatePin(pin: String) {
        _uiState.update {
            it.copy(
                pin = pin
            )
        }
    }

    fun loginUser() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loginStatus = LoginStatus.LOADING
                )
            }
            val loginRequestBody = LoginRequestBody(
                phoneNumber = uiState.value.phoneNumber,
                pin = uiState.value.pin
            )

            try {
                val response = apiRepository.login(loginRequestBody = loginRequestBody)
                if(response.isSuccessful) {
                    withContext(Dispatchers.IO) {
                        val users = dbRepository.getUsers().first()

                        if(users.isEmpty()) {
                            dbRepository.insertUser(
                                userDetails = UserDetails(
                                    userId = response.body()?.data?.user?.userId!!,
                                    username = response.body()?.data?.user?.name,
                                    phoneNumber = response.body()?.data?.user?.phoneNumber,
                                    email = response.body()?.data?.user?.email,
                                    token = response.body()?.data?.token,
                                )
                            )
                        } else {
                            val user = users[0]
                            dbRepository.updateUser(
                                userDetails = user.copy(
                                    username = response.body()?.data?.user?.name,
                                    phoneNumber = response.body()?.data?.user?.phoneNumber,
                                    email = response.body()?.data?.user?.email,
                                    pin = uiState.value.pin,
                                    token = response.body()?.data?.token,
                                )
                            )
                        }

                        var user = dbRepository.getUser(userId = response.body()?.data?.user?.userId!!).first()

                        while(user.username == null) {
                            delay(1000)
                            user = dbRepository.getUser(userId = response.body()?.data?.user?.userId!!).first()
                        }

                        _uiState.update {
                            it.copy(
                                loginMessage = "Login successful",
                                loginStatus = LoginStatus.SUCCESS
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        Log.e("login_response_err", response.toString())
                        it.copy(
                            loginMessage = response.message(),
                            loginStatus = LoginStatus.FAIL
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    Log.e("login_exception_err", e.toString())
                    it.copy(
                        loginMessage = e.message.toString(),
                        loginStatus = LoginStatus.FAIL
                    )
                }
            }
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                loginStatus = LoginStatus.INITIAL
            )
        }
    }

    fun enableButton() {
        _uiState.update {
            it.copy(
                buttonEnabled = uiState.value.phoneNumber.isNotEmpty() &&
                        uiState.value.pin.length == 6
            )
        }
    }

    init {
        _uiState.update {
            it.copy(
                phoneNumber = savedStateHandle[LoginScreenDestination.phoneNumber] ?: "",
                pin = savedStateHandle[LoginScreenDestination.pin] ?: ""
            )
        }
        enableButton()
    }
}