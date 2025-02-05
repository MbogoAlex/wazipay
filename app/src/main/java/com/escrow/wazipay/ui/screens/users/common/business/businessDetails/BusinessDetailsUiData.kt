package com.escrow.wazipay.ui.screens.users.common.business.businessDetails

import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.business.emptyBusinessData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.ui.screens.users.common.business.LoadBusinessStatus

data class BusinessDetailsUiData(
    val userDetails: UserDetails = UserDetails(),
    val businessData: BusinessData = emptyBusinessData,
    val productName: String = "",
    val newProductName: String = "",
    val businessName: String = "",
    val businessDescription: String = "",
    val location: String = "",
    val role: Role = Role.BUYER,
    val businessId: String? = null,
    val unauthorized: Boolean = false,
    val loadBusinessStatus: LoadBusinessStatus = LoadBusinessStatus.LOADING,
    val deletingStatus: DeletingStatus = DeletingStatus.INITIAL,
    val editingStatus: EditingStatus = EditingStatus.INITIAL,
    val archivingStatus: ArchivingStatus = ArchivingStatus.INITIAL
)
