package com.escrow.wazipay

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.escrow.wazipay.ui.screens.auth.LoginViewModel
import com.escrow.wazipay.ui.screens.auth.PinViewModel
import com.escrow.wazipay.ui.screens.auth.RegistrationViewModel
import com.escrow.wazipay.ui.screens.dashboard.DashboardViewModel
import com.escrow.wazipay.ui.screens.users.common.business.businessDetails.BusinessDetailsViewModel
import com.escrow.wazipay.ui.screens.users.common.business.businessList.BusinessViewModel
import com.escrow.wazipay.ui.screens.users.common.invoice.invoiceDetails.InvoiceDetailsViewModel
import com.escrow.wazipay.ui.screens.users.common.invoice.invoicesList.InvoicesViewModel
import com.escrow.wazipay.ui.screens.users.common.order.orderCreation.OrderCreationViewModel
import com.escrow.wazipay.ui.screens.users.common.order.orderDetails.OrderDetailsViewModel
import com.escrow.wazipay.ui.screens.users.common.order.ordersList.OrdersViewModel
import com.escrow.wazipay.ui.screens.users.common.profile.ProfileViewModel
import com.escrow.wazipay.ui.screens.users.common.transaction.transactionsList.TransactionsViewModel
import com.escrow.wazipay.ui.screens.users.common.wallet.deposit.DepositViewModel
import com.escrow.wazipay.ui.screens.users.common.wallet.withdrawal.WithdrawalViewModel
import com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment.BusinessSelectionViewModel
import com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment.InvoiceCreationViewModel
import com.escrow.wazipay.ui.screens.users.specific.buyer.dashboard.BuyerDashboardViewModel
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.CourierAssignmentViewModel
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.CourierSelectionViewModel
import com.escrow.wazipay.ui.screens.users.specific.merchant.dashboard.MerchantDashboardViewModel
import com.escrow.wazipay.ui.screens.users.specific.merchant.invoiceCreation.BuyerSelectionViewModel
import com.escrow.wazipay.ui.screens.users.specific.merchant.invoiceCreation.InvoiceIssuanceViewModel
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
                dbRepository = wazipayApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
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

        initializer {
            ProfileViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository
            )
        }

        initializer {
            BuyerSelectionViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            InvoiceDetailsViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            InvoiceIssuanceViewModel(
                apiRepository = wazipayApplication().container.apiRepository,
                dbRepository = wazipayApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }
    }
}

fun CreationExtras.wazipayApplication(): Wazipay =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Wazipay)