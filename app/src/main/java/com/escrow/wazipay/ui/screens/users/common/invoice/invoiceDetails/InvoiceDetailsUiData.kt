package com.escrow.wazipay.ui.screens.users.common.invoice.invoiceDetails

import com.escrow.wazipay.data.network.models.invoice.InvoiceData
import com.escrow.wazipay.data.network.models.invoice.emptyInvoice
import com.escrow.wazipay.data.network.models.order.OrderData
import com.escrow.wazipay.data.network.models.order.emptyOrderData
import com.escrow.wazipay.data.network.models.user.UserContactData
import com.escrow.wazipay.data.network.models.user.emptyUserContactData
import com.escrow.wazipay.data.network.models.wallet.UserWalletData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment.PaymentMethod
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.LoadingStatus

data class InvoiceDetailsUiData(
    val userDetails: UserDetails = UserDetails(),
    val role: Role = Role.BUYER,
    val invoiceData: InvoiceData = emptyInvoice,
    val orderData: OrderData = emptyOrderData,
    val userWalletData: UserWalletData = UserWalletData(0, 0.0, UserContactData(0, "", "", "")),
    val phoneNumber: String = "",
    val buyer: UserContactData = emptyUserContactData,
    val merchant: UserContactData = emptyUserContactData,
    val newOrderId: Int = 0,
    val paymentMethod: PaymentMethod = PaymentMethod.WAZIPAY,
    val buttonEnabled: Boolean = false,
    val unauthorized: Boolean = false,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
