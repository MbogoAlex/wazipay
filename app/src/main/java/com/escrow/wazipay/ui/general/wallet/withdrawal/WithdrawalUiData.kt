package com.escrow.wazipay.ui.general.wallet.withdrawal

import com.escrow.wazipay.data.network.models.user.userContactData
import com.escrow.wazipay.data.network.models.wallet.UserWalletData
import com.escrow.wazipay.data.room.models.UserDetails

data class WithdrawalUiData(
    val userDetails: UserDetails = UserDetails(),
    val phoneNumber: String = "",
    val withdrawalAmount: String = "",
    val newBalance: Double = 0.0,
    val withdrawMessage: String = "",
    val profile: String? = null,
    val userWalletData: UserWalletData = UserWalletData(0, 0.0, userContactData),
    val buttonEnabled: Boolean = false,
    val withdrawalStatus: WithdrawalStatus = WithdrawalStatus.INITIAL
)
