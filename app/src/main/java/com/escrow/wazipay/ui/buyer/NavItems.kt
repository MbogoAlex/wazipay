package com.escrow.wazipay.ui.buyer

enum class NavBarItem {
    HOME,
    TRANSACTIONS,
    ORDERS,
    PROFILE
}

data class NavItem(
    val name: String,
    val icon: Int,
    val tab: NavBarItem
)