package com.escrow.wazipay

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.escrow.wazipay.ui.auth.LoginViewModel
import com.escrow.wazipay.ui.auth.PinViewModel
import com.escrow.wazipay.ui.auth.RegistrationViewModel
import com.escrow.wazipay.ui.buyer.BuyerDashboardViewModel
import com.escrow.wazipay.ui.buyer.invoice.BusinessSelectionViewModel
import com.escrow.wazipay.ui.buyer.invoice.InvoiceCreationViewModel
import com.escrow.wazipay.ui.dashboard.DashboardViewModel
import com.escrow.wazipay.ui.general.business.BusinessDetailsViewModel
import com.escrow.wazipay.ui.general.business.BusinessViewModel
import com.escrow.wazipay.ui.general.invoice.InvoicesViewModel
import com.escrow.wazipay.ui.general.order.OrderCreationViewModel
import com.escrow.wazipay.ui.general.order.OrderDetailsViewModel
import com.escrow.wazipay.ui.general.order.OrdersViewModel
import com.escrow.wazipay.ui.general.transaction.TransactionsViewModel
import com.escrow.wazipay.ui.general.wallet.deposit.DepositViewModel
import com.escrow.wazipay.ui.general.wallet.withdrawal.WithdrawalViewModel
import com.escrow.wazipay.ui.merchant.MerchantDashboardViewModel
import com.escrow.wazipay.ui.merchant.courier.CourierAssignmentViewModel
import com.escrow.wazipay.ui.merchant.courier.CourierSelectionViewModel
import com.escrow.wazipay.ui.start.SplashViewModel

object AppViewModelFactory {
    @RequiresApi(Build.VERSION_CODES.O)
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
            DashboardViewModel(
                dbRepository = wazipayApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            OrdersViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            DepositViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            WithdrawalViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            BusinessViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository
            )
        }

        initializer {
            BusinessDetailsViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            OrderCreationViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            InvoicesViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository
            )
        }

        initializer {
            TransactionsViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository
            )
        }

        initializer {
            MerchantDashboardViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository
            )
        }

        initializer {
            BusinessSelectionViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository
            )
        }

        initializer {
            InvoiceCreationViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            OrderDetailsViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            CourierSelectionViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            CourierAssignmentViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }
    }
}

fun CreationExtras.wazipayApplication(): Wazipay =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Wazipay)