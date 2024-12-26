package com.escrow.wazipay.data.network.models.business

import com.escrow.wazipay.data.network.models.order.userContactData

val businessData = BusinessData(
    id = 1,
    name = "Gas Delivery",
    description = "We deliver gas cooker stoves",
    products = listOf("Total Gas", "Taifa Gas", "Meko"),
    createdAt = "2024-12-26T08:57:48.085977",
    archived = false,
    archivedAt = null,
    owner = userContactData
)

val businesses = List(10) {index ->
    BusinessData(
        id = 1 + index,
        name = "Gas Delivery",
        description = "We deliver gas cooker stoves",
        products = listOf("Total Gas", "Taifa Gas", "Meko"),
        createdAt = "2024-12-26T08:57:48.085977",
        archived = false,
        archivedAt = null,
        owner = userContactData
    )
}