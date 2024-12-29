package com.escrow.wazipay.ui.general.order

data class OrderCreationUiData(
    val productName: String = "",
    val description: String = "",
    val amount: String = "",
    val businessId: String? = null,
    val buttonEnabled: Boolean = false,
    val orderCreationStatus: OrderCreationStatus = OrderCreationStatus.INITIAL
)
