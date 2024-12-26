package com.escrow.wazipay

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.escrow.wazipay.ui.auth.LoginViewModel
import com.escrow.wazipay.ui.auth.PinViewModel
import com.escrow.wazipay.ui.auth.RegistrationViewModel
import com.escrow.wazipay.ui.buyer.BuyerDashboardViewModel
import com.escrow.wazipay.ui.general.orders.OrdersViewModel
import com.escrow.wazipay.ui.start.SplashViewModel

object AppViewModelFactory {
    val Factory = viewModelFactory {

        initializer {
            MainActivityViewModel(
                dbRepository = wazipayApplication().container.dbRepository
            )
        }

        initializer {
            SplashViewModel(
                dbRepository = wazipayApplication().container.dbRepository
            )
        }
        initializer {
            RegistrationViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository
            )
        }

        initializer {
            LoginViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            PinViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository
            )
        }

        initializer {
            BuyerDashboardViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository
            )
        }

        initializer {
            OrdersViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository
            )
        }
    }
}

fun CreationExtras.wazipayApplication(): Wazipay =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Wazipay)