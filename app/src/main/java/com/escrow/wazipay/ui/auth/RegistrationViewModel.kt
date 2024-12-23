package com.escrow.wazipay.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.models.common.RegistrationRequestBody
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.data.room.repository.DBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationUiData())
    val uiState: StateFlow<RegistrationUiData> = _uiState.asStateFlow()

    fun updateUsername(name: String) {
        _uiState.update {
            it.copy(
                username = name
            )
        }
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _uiState.update {
            it.copy(
                phoneNumber = phoneNumber
            )
        }
    }

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email
            )
        }
    }

    fun registerUser() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    registrationStatus = RegistrationStatus.LOADING
                )
            }
            val registrationRequestBody = RegistrationRequestBody(
                name = uiState.value.username,
                phoneNumber = uiState.value.phoneNumber,
                email = uiState.value.email
            )

            try {
                val response = apiRepository.registerUser(
                    registrationRequestBody = registrationRequestBody
                )

                if(response.isSuccessful) {
                    val userDetails = UserDetails(
                        userId = response.body()?.data?.userId!!
                    )
                    withContext(Dispatchers.IO) {
                        dbRepository.insertUser(
                            userDetails = userDetails
                        )

                        var users = dbRepository.getUsers().first()
                        while(users.isEmpty()) {
                            users = dbRepository.getUsers().first()
                        }
                    }
                    _uiState.update {
                        it.copy(
                            registrationMessage = "Account created successfully",
                            registrationStatus = RegistrationStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        Log.e("user_registration_response_err", response.toString())
                        it.copy(
                            registrationMessage = response.message(),
                            registrationStatus = RegistrationStatus.FAIL
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("user_registration_exception_e", e.toString())
                _uiState.update {
                    it.copy(
                        registrationMessage = e.message.toString(),
                        registrationStatus = RegistrationStatus.FAIL
                    )
                }
            }
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                registrationStatus = RegistrationStatus.INITIAL
            )
        }
    }

    fun enableButton() {
        _uiState.update {
            it.copy(
                buttonEnabled = uiState.value.username.isNotEmpty() &&
                uiState.value.phoneNumber.isNotEmpty() &&
                uiState.value.email.isNotEmpty()
            )
        }
    }
}