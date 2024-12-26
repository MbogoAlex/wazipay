package com.escrow.wazipay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.room.models.DarkMode
import com.escrow.wazipay.data.room.repository.DBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel(
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(MainActivityUiData())
    val uiState: StateFlow<MainActivityUiData> = _uiState.asStateFlow()

    private fun getTheme() {
        var darkMode: DarkMode?
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                darkMode = dbRepository.getTheme().first()

                if(darkMode == null) {
                    dbRepository.createTheme(DarkMode(darkMode = false))
                }

                dbRepository.getTheme().collect { theme ->
                    _uiState.update {
                        it.copy(
                            darkMode = theme!!
                        )
                    }
                }
            }
        }
    }

    fun switchTheme() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.changeTheme(
                    _uiState.value.darkMode.copy(
                        darkMode = !_uiState.value.darkMode.darkMode
                    )
                )
            }
        }
    }

    init {
        getTheme()
    }
}