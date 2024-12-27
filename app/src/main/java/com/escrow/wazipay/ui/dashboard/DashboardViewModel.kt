package com.escrow.wazipay.ui.dashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.escrow.wazipay.ui.general.wallet.DepositUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DashboardViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(DepositUiData())
    val uiState: StateFlow<DepositUiData> = _uiState.asStateFlow()

    fun switchProfile(profile: String) {
        _uiState.update {
            it.copy(
                profile = profile
            )
        }
    }

    init {
        _uiState.update {
            it.copy(
                profile = savedStateHandle[DashboardScreenDestination.profile]
            )
        }
    }
}