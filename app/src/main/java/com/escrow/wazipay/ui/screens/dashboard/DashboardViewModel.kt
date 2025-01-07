package com.escrow.wazipay.ui.screens.dashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.data.room.repository.DBRepository
import com.escrow.wazipay.ui.screens.users.common.NavBarItem
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

    private fun getUserDetails() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.getUsers().collect { users ->
                    _uiState.update {
                        it.copy(
                            userDetails = if(users.isNotEmpty()) users[0] else UserDetails()
                        )
                    }
                }
            }
        }
    }

    fun deleteUsers() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.deleteUsers()
            }
        }
    }

    init {
        _uiState.update {
            it.copy(
                child = savedStateHandle.get<String>(DashboardScreenDestination.child)?.let { nav -> com.escrow.wazipay.ui.screens.users.common.NavBarItem.valueOf(nav) } ?: com.escrow.wazipay.ui.screens.users.common.NavBarItem.HOME
            )
        }
        getUserRole()
        getUserDetails()
    }
}