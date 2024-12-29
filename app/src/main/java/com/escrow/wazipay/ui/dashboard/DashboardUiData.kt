package com.escrow.wazipay.ui.dashboard

import com.escrow.wazipay.ui.general.NavBarItem

data class DashboardUiData(
    val profile: String = "Buyer",
    val child: NavBarItem = NavBarItem.HOME
)
