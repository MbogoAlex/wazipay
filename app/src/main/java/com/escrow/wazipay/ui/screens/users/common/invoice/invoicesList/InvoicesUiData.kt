package com.escrow.wazipay.ui.screens.users.common.invoice.invoicesList

import com.escrow.wazipay.data.network.models.invoice.InvoiceData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.data.room.models.UserRole
import com.escrow.wazipay.ui.screens.users.common.invoice.LoadInvoicesStatus
import com.escrow.wazipay.ui.screens.users.common.invoice.InvoiceStatus

data class InvoicesUiData(
    val userDetails: UserDetails = UserDetails(),
    val userRole: UserRole = UserRole(0, Role.BUYER),
    val invoices: List<InvoiceData> = emptyList(),
    val userAuthorized: Boolean = true,
    val invoiceStatus: InvoiceStatus? = null,
    val selectedStatus: String = "All",
    val statuses: List<String> = listOf("All", "Complete", "Pending", "Rejected", "Cancelled"),
    val loadInvoicesStatus: LoadInvoicesStatus = LoadInvoicesStatus.INITIAL
)
