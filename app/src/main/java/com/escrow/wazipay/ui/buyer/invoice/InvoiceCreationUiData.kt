package com.escrow.wazipay.ui.buyer.invoice

import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.business.businesses
import com.escrow.wazipay.data.network.models.user.userContactData
import com.escrow.wazipay.data.network.models.wallet.UserWalletData

data class InvoiceCreationUiData(
    val title: String = "",
    val description: String = "",
    val amount: String = "",
    val phoneNumber: String = "",
    val orderId: String = "",
    val businessData: BusinessData = businesses[0],
    val userWalletData: UserWalletData = UserWalletData(0, 0.0, userContactData),
    val paymentMethod: PaymentMethod = PaymentMethod.WAZIPAY,
    val invoiceCreationStatus: InvoiceCreationStatus = InvoiceCreationStatus.INITIAL
)
