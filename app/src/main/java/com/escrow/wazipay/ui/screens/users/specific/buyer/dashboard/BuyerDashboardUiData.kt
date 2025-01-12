package com.escrow.wazipay.ui.screens.users.specific.buyer.dashboard

import com.escrow.wazipay.data.network.models.invoice.InvoiceData
import com.escrow.wazipay.data.network.models.order.OrderData
import com.escrow.wazipay.data.network.models.transaction.TransactionData
import com.escrow.wazipay.data.network.models.user.UserContactData
import com.escrow.wazipay.data.network.models.user.UserDetailsData
import com.escrow.wazipay.data.network.models.wallet.UserWalletData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.ui.screens.users.common.invoice.LoadInvoicesStatus
import com.escrow.wazipay.ui.screens.users.common.transaction.LoadTransactionsStatus
import com.escrow.wazipay.ui.screens.users.common.enums.LoadUserStatus
import com.escrow.wazipay.ui.screens.users.common.wallet.LoadWalletStatus
import com.escrow.wazipay.ui.screens.users.common.order.LoadOrdersStatus

data class BuyerDashboardUiData(
    val userDetails: UserDetails = UserDetails(),
    val role: Role = Role.BUYER,
    val userDetailsData: UserDetailsData = UserDetailsData(
        userId = 1,
        username = "",
        email = "",
        phoneNumber = "",
        createdAt = "",
        archived = false,
        archivedAt = null,
        verified = true,
        verifiedAt = null,
        verificationStatus = "",
        roles = emptyList(),
        idFront = "",
        idBack = ""
    ),
    val userWalletData: UserWalletData = UserWalletData(
        id = 1,
        balance = 0.0,
        owner = UserContactData(id = 1, username = "", phoneNumber = "", email = "")
    ),
    val orders: List<OrderData> = emptyList(),
    val invoices: List<InvoiceData> = emptyList(),
    val pendingInvoices: List<InvoiceData> = emptyList(),
    val transactions: List<TransactionData> = emptyList(),
    val loadUserStatus: LoadUserStatus = LoadUserStatus.INITIAL,
    val loadInvoicesStatus: LoadInvoicesStatus = LoadInvoicesStatus.INITIAL,
    val loadOrdersStatus: LoadOrdersStatus = LoadOrdersStatus.INITIAL,
    val loadTransactionsStatus: LoadTransactionsStatus = LoadTransactionsStatus.INITIAL,
    val loadWalletStatus: LoadWalletStatus = LoadWalletStatus.INITIAL,
    val unauthorized: Boolean = false,
)
