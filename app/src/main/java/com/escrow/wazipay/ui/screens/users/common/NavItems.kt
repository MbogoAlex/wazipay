package com.escrow.wazipay.ui.screens.users.common

enum class NavBarItem {
//    Buyer
    HOME,
    PAY_BUSINESS,
    TRANSACTIONS,
    ORDERS,
    PROFILE,
    BUSINESSES,
    INVOICES,
    COURIER_ASSIGNMENT,
    ISSUE_INVOICE,
    ADD_BUSINESS,

//    Seller
    SHOPS,

}

data class NavItem(
    val name: String,
    val icon: Int,
    val tab: com.escrow.wazipay.ui.screens.users.common.NavBarItem
)