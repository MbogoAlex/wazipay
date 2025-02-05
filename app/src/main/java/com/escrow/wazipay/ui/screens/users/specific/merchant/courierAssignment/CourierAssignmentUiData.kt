package com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment

import com.escrow.wazipay.data.network.models.order.OrderData
import com.escrow.wazipay.data.network.models.order.emptyOrderData
import com.escrow.wazipay.data.network.models.user.UserContactData
import com.escrow.wazipay.data.network.models.user.UserDetailsData
import com.escrow.wazipay.data.network.models.user.emptyUser
import com.escrow.wazipay.data.network.models.wallet.UserWalletData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment.PaymentMethod

data class CourierAssignmentUiData(
    val userDetails: UserDetails = UserDetails(),
    val role: Role = Role.BUYER,
    val orderData: OrderData = emptyOrderData,
    val courier: UserDetailsData = emptyUser,
    val userWalletData: UserWalletData = UserWalletData(0, 0.0, UserContactData(0, "", "", "")),
    val amount: String = "",
    val phoneNumber: String = "",
    val buttonEnabled: Boolean = false,
    val paymentMethod: PaymentMethod = PaymentMethod.WAZIPAY_ESCROW,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
