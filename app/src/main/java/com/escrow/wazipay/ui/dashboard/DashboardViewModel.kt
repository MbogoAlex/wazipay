package com.escrow.wazipay.ui.dashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserRole
import com.escrow.wazipay.data.room.repository.DBRepository
import com.escrow.wazipay.ui.general.NavBarItem
import com.escrow.wazipay.ui.general.wallet.deposit.DepositUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardViewModel(
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiData())
    val uiState: StateFlow<DashboardUiData> = _uiState.asStateFlow()

    fun switchRole(role: Role) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.updateUserRole(
                    _uiState.value.userRole.copy(
                        role = role
                    )
                )
            }
        }
    }

    fun changeTab(tab: NavBarItem) {
        _uiState.update {
            it.copy(
                child = tab
            )
        }
    }

    private fun getUserRole() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.getUserRole().collect { userRole ->
                    _uiState.update {
                        it.copy(
                            userRole = userRole!!
                        )
                    }
                }
            }
        }
    }

    init {
        _uiState.update {
            it.copy(
                child = savedStateHandle.get<String>(DashboardScreenDestination.child)?.let { nav -> NavBarItem.valueOf(nav) } ?: NavBarItem.HOME
            )
        }
        getUserRole()
    }
}