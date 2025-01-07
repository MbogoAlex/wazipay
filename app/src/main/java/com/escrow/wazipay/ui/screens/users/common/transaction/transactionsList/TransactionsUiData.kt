package com.escrow.wazipay.ui.screens.users.common.transaction.transactionsList

import com.escrow.wazipay.data.network.models.transaction.TransactionData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.ui.screens.users.common.transaction.LoadTransactionsStatus

data class TransactionsUiData(
    val userDetails: UserDetails = UserDetails(),
    val role: Role = Role.BUYER,
    val transactions: List<TransactionData> = emptyList(),
    val startDate: String = "",
    val endDate: String = "",
    val searchText: String = "",
    val userAuthorized: Boolean = true,
    val loadTransactionsStatus: LoadTransactionsStatus = LoadTransactionsStatus.INITIAL,
)
