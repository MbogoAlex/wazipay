package com.escrow.wazipay.ui.general.invoice

import com.escrow.wazipay.data.network.models.invoice.InvoiceData
import com.escrow.wazipay.data.room.models.UserDetails

data class InvoicesUiData(
    val userDetails: UserDetails = UserDetails(),
    val invoices: List<InvoiceData> = emptyList(),
    val userAuthorized: Boolean = true,
    val invoiceStatus: InvoiceStatus? = null,
    val loadInvoicesStatus: LoadInvoicesStatus = LoadInvoicesStatus.INITIAL
)
