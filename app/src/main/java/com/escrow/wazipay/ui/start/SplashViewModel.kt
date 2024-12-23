package com.escrow.wazipay.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class SplashViewModel(
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(SplashUiData())
    val uiState: StateFlow<SplashUiData> = _uiState.asStateFlow()

    private fun getUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val users = dbRepository.getUsers().first()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        userDetails = if(users.isEmpty()) UserDetails() else users[0]
                    )
                }
            }
        }
    }

    fun stopNavigating() {
        _uiState.update {
            it.copy(
                isNavigating = false
            )
        }
    }

    init {
        getUser()
    }

}
