package com.escrow.wazipay.ui.general

enum class NavBarItem {
//    Buyer
    HOME,
    TRANSACTIONS,
    ORDERS,
    PROFILE,

//    Seller
    SHOPS,

}

data class NavItem(
    val name: String,
    val icon: Int,
    val tab: NavBarItem
)