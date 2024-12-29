package com.escrow.wazipay.ui.dashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.escrow.wazipay.ui.general.NavBarItem
import com.escrow.wazipay.ui.general.wallet.deposit.DepositUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DashboardViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiData())
    val uiState: StateFlow<DashboardUiData> = _uiState.asStateFlow()

    fun switchProfile(profile: String) {
        _uiState.update {
            it.copy(
                profile = profile
            )
        }
    }

    fun changeTab(tab: NavBarItem) {
        _uiState.update {
            it.copy(
                child = tab
            )
        }
    }

    init {
        _uiState.update {
            it.copy(
                profile = savedStateHandle[DashboardScreenDestination.profile] ?: "Buyer",
                child = savedStateHandle.get<String>(DashboardScreenDestination.child)?.let { nav -> NavBarItem.valueOf(nav) } ?: NavBarItem.HOME
            )
        }
    }
}