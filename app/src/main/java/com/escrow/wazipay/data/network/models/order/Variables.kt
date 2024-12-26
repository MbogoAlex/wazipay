package com.escrow.wazipay.data.network.models.order

import com.escrow.wazipay.data.network.models.business.businessData
import com.escrow.wazipay.data.network.models.user.UserContactData

val userContactData = UserContactData(
    id = 1,
    username = "Alex Mbogo",
    phoneNumber = "0794649026",
    email = "alex@gmail.com"
)

val orderData = OrderData(
    id = 1,
    orderCode = "1234",
    name = "6 Kg Gas",
    description = "6 kg total Gas",
    productCost = 1500.0,
    deliveryCost = null,
    orderStage = "COMPLETE",
    merchant = userContactData,
    buyer = userContactData,
    courier = userContactData,
    business = businessData,
    createdAt = "2024-12-26T11:24:46.215144",
    completedAt = "2024-12-26T11:24:46.215144",
    refundedAt = null,
    cancelledAt = null,
    refundReason = null,
    cancellationReason = null
)

val orders = List(10) { index ->
    OrderData(
        id = index + 1,
        orderCode = "123${index + 1}", // Unique order code for each order
        name = "6 Kg Gas",
        description = "6 kg total Gas",
        productCost = 1500.0 + index * 100, // Example: increment cost for variety
        deliveryCost = null,
        orderStage = "PENDING_PICKUP",
        merchant = userContactData,
        buyer = userContactData,
        courier = userContactData,
        business = businessData,
        createdAt = "2024-12-26T11:24:46.215144",
        completedAt = null,
        refundedAt = null,
        cancelledAt = null,
        refundReason = null,
        cancellationReason = null
    )
}

