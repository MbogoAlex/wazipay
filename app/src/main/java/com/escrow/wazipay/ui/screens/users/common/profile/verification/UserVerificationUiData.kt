package com.escrow.wazipay.ui.screens.users.common.profile.verification

import android.net.Uri
import com.escrow.wazipay.data.network.models.user.UserDetailsData
import com.escrow.wazipay.data.network.models.user.emptyUser
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.LoadingStatus

data class UserVerificationUiData(
    val userDetails: UserDetails = UserDetails(),
    val userDetailsData: UserDetailsData = emptyUser,
    val idFront: Uri? = null,
    val idBack: Uri? = null,
    val buttonEnabled: Boolean = false,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
