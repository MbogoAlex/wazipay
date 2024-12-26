package com.escrow.wazipay.data.network.models.transaction

import com.escrow.wazipay.data.network.models.invoice.invoiceData
import com.escrow.wazipay.data.network.models.order.orderData

val transactionData = TransactionData(
    id = 1,
    transactionCode = "e941893d-3d94-4d3e-9e39-35b975797fab",
    transactionType = "ESCROW_PAYMENT",
    createdAt = "2024-12-26T09:31:55.283796",
    amount = 1000.00,
    order = orderData,
    invoice = null
)

val transactions = List(10) { index ->
    TransactionData(
        id = 1 + index,
        transactionCode = "e941893d-3d94-4d3e-9e39-35b975797fab",
        transactionType = "ESCROW_PAYMENT",
        createdAt = "2024-12-26T09:31:55.283796",
        amount = 1000.00,
        order = orderData,
        invoice = invoiceData
    )
}