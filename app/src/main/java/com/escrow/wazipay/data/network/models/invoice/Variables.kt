package com.escrow.wazipay.data.network.models.invoice

import com.escrow.wazipay.data.network.models.business.businessData
import com.escrow.wazipay.data.network.models.transaction.transactionData
import com.escrow.wazipay.data.network.models.user.emptyUser
import com.escrow.wazipay.data.network.models.user.emptyUserContactData
import com.escrow.wazipay.data.network.models.user.userContactData

val invoiceData = InvoiceData(
    id = 1,
    title = "13kg Gas",
    description = "Gas for 13kg",
    amount = 1000.00,
    businessId = 1,
    merchant = userContactData,
    buyer = userContactData,
    invoiceStatus = "PENDING",
    createdAt = "2024-12-26T09:31:55.283796",
    rejectedAt = null,
    cancelledAt = null,
    rejectionReason = null,
    cancellationReason = null,
    transactionId = 1,
    orderId = 1,
    paymentLink = "192.168.100.5:8000/api/user/invoice/1"
)

val emptyInvoice = InvoiceData(
    id = 1,
    title = "",
    description = "",
    amount = 0.00,
    businessId = 1,
    merchant = emptyUserContactData,
    buyer = emptyUserContactData,
    invoiceStatus = "PENDING",
    createdAt = "",
    rejectedAt = null,
    cancelledAt = null,
    rejectionReason = null,
    cancellationReason = null,
    transactionId = 1,
    orderId = 1,
    paymentLink = ""
)

val invoices = List(10) { index ->
    InvoiceData(
        id = 1 + index,
        title = "13kg Gas",
        description = "Gas for 13kg",
        amount = 1000.00,
        businessId = 1,
        merchant = userContactData,
        buyer = userContactData,
        invoiceStatus = "PENDING",
        createdAt = "2024-12-26T09:31:55.283796",
        rejectedAt = null,
        cancelledAt = null,
        rejectionReason = null,
        cancellationReason = null,
        transactionId = 1,
        orderId = 1,
        paymentLink = "192.168.100.5:8000/api/user/invoice/1"
    )
}