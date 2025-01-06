package com.escrow.wazipay.data.network.models.business

import com.escrow.wazipay.data.network.models.order.userContactData
import com.escrow.wazipay.data.network.models.user.UserContactData

val businessData = BusinessData(
    id = 1,
    name = "Gas Delivery",
    description = "We deliver gas cooker stoves",
    products = listOf("Total Gas", "Taifa Gas", "Meko"),
    createdAt = "2024-12-26T08:57:48.085977",
    archived = false,
    archivedAt = null,
    owner = UserContactData(
        id = 1,
        username = "Alex Mbogo",
        phoneNumber = "0794649026",
        email = "alex@gmail.com"
    )
)

val emptyBusinessData = BusinessData(
    id = 0,
    name = "",
    description = "",
    products = listOf(),
    createdAt = "",
    archivedAt = null,
    archived = false,
    owner = null
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
        owner = UserContactData(
            id = 1,
            username = "Alex Mbogo",
            phoneNumber = "0794649026",
            email = "alex@gmail.com"
        )
    )
}