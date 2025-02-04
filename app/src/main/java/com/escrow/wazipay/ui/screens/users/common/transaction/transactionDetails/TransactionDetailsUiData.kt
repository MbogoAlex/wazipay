package com.escrow.wazipay.ui.screens.users.common.transaction.transactionDetails

import com.escrow.wazipay.data.network.models.transaction.TransactionData
import com.escrow.wazipay.data.network.models.transaction.transactions
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails

data class TransactionDetailsUiData(
    val userDetails: UserDetails = UserDetails(),
    val role: Role = Role.BUYER,
    val transactionData: TransactionData = transactions[0],
    val loadTransactionStatus: LoadTransactionStatus = LoadTransactionStatus.LOADING
)
