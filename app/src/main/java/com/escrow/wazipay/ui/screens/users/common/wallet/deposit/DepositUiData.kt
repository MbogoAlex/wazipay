package com.escrow.wazipay.ui.screens.users.common.wallet.deposit

import com.escrow.wazipay.data.network.models.user.userContactData
import com.escrow.wazipay.data.network.models.wallet.UserWalletData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails

data class DepositUiData(
    val userDetails: UserDetails = UserDetails(),
    val role: Role = Role.BUYER,
    val userWalletData: UserWalletData = UserWalletData(1, 0.0, userContactData),
    val amount: String = "",
    val phoneNumber: String = "",
    val newBalance: Double = 0.0,
    val buttonEnabled: Boolean = false,
    val depositMessage: String = "",
    val depositStatus: DepositStatus = DepositStatus.INITIAL
)
