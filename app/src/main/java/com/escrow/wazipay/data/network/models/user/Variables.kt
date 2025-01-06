package com.escrow.wazipay.data.network.models.user

val userContactData = UserContactData(
    id = 1,
    username = "Alex Mbogo",
    phoneNumber = "0794649026",
    email = "mbogo@gmail.com"
)

val userDetailsData = UserDetailsData(
    userId = 1,
    username = "Alex Mbogo",
    email = "mbogo@gmail.com",
    phoneNumber = "0794649026",
    createdAt = "2024-12-26T07:51:01.979383",
    archived = false,
    archivedAt = null,
    verified = true,
    verifiedAt = null,
    verificationStatus = "VERIFIED",
    roles = listOf("BUYER", "MERCHANT", "ADMIN"),
    idFront = "http://192.168.100.5:8000/images/1735188979592_Screenshotfrom2024-01-3118-44-22.png",
    idBack = "http://192.168.100.5:8000/images/1735188979595_Screenshotfrom2023-02-0217-22-42.png"
)

val emptyUser = UserDetailsData(
    userId = 1,
    username = "",
    email = "",
    phoneNumber = "",
    createdAt = "",
    archived = false,
    archivedAt = null,
    verified = true,
    verifiedAt = null,
    verificationStatus = "VERIFIED",
    roles = listOf(""),
    idFront = "",
    idBack = ""
)

val users = List(10) {index ->
    UserDetailsData(
        userId = 1 + index,
        username = "Alex Mbogo",
        email = "mbogo@gmail.com",
        phoneNumber = "0794649026",
        createdAt = "2024-12-26T07:51:01.979383",
        archived = false,
        archivedAt = null,
        verified = true,
        verifiedAt = null,
        verificationStatus = "VERIFIED",
        roles = listOf("BUYER", "MERCHANT", "ADMIN"),
        idFront = "http://192.168.100.5:8000/images/1735188979592_Screenshotfrom2024-01-3118-44-22.png",
        idBack = "http://192.168.100.5:8000/images/1735188979595_Screenshotfrom2023-02-0217-22-42.png"
    )
}