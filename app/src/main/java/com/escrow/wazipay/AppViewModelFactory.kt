package com.escrow.wazipay

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.escrow.wazipay.ui.auth.RegistrationViewModel

object AppViewModelFactory {
    val Factory = viewModelFactory {
        initializer {
            RegistrationViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository
            )
        }
    }
}

fun CreationExtras.wazipayApplication(): Wazipay =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Wazipay)