package com.escrow.wazipay.data.network.models.business

import com.escrow.wazipay.data.network.models.user.UserContactData

val product = ProductData(
    productId = 1,
    name = "Product 1",
    businessId = 1
)

val products = List(10) {index ->
    ProductData(
        productId = 1 + index,
        name = "Product $index",
        businessId = 1
    )
}

val businessData = BusinessData(
    id = 1,
    name = "Gas Delivery",
    description = "We deliver gas cooker stoves",
    products = products,
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
        products = products,
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