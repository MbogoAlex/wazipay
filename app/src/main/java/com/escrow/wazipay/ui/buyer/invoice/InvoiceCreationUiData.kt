package com.escrow.wazipay.ui.buyer.invoice

import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.business.businesses
import com.escrow.wazipay.data.network.models.user.userContactData
import com.escrow.wazipay.data.network.models.wallet.UserWalletData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails

data class InvoiceCreationUiData(
    val userDetails: UserDetails = UserDetails(),
    val role: Role = Role.BUYER,
    val title: String = "",
    val description: String = "",
    val amount: String = "",
    val phoneNumber: String = "",
    val orderId: String = "",
    val invoiceId: String = "",
    val businessData: BusinessData = businesses[0],
    val userWalletData: UserWalletData = UserWalletData(0, 0.0, userContactData),
    val paymentMethod: PaymentMethod = PaymentMethod.WAZIPAY,
    val unauthorized: Boolean = false,
    val buttonEnabled: Boolean = false,
    val invoiceCreationStatus: InvoiceCreationStatus = InvoiceCreationStatus.INITIAL
)
