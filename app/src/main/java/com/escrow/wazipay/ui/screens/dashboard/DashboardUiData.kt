package com.escrow.wazipay.ui.screens.dashboard

import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.data.room.models.UserRole
import com.escrow.wazipay.ui.screens.users.common.NavBarItem

data class DashboardUiData(
    val userDetails: UserDetails = UserDetails(),
    val userRole: UserRole = UserRole(0, Role.BUYER),
    val child: NavBarItem = NavBarItem.HOME,
)
