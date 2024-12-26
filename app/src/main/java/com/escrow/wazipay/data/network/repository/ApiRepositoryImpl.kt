package com.escrow.wazipay.data.network.repository

import com.escrow.wazipay.data.network.models.common.LoginRequestBody
import com.escrow.wazipay.data.network.models.common.LoginResponseBody
import com.escrow.wazipay.data.network.models.common.RegistrationRequestBody
import com.escrow.wazipay.data.network.models.common.RegistrationResponseBody
import com.escrow.wazipay.data.network.models.common.SetPinRequestBody
import com.escrow.wazipay.data.network.models.common.SetPinResponseBody
import com.escrow.wazipay.data.network.models.invoice.InvoiceResponseBody
import com.escrow.wazipay.data.network.models.invoice.InvoicesResponseBody
import com.escrow.wazipay.data.network.models.order.OrderResponseBody
import com.escrow.wazipay.data.network.models.order.OrdersResponseBody
import com.escrow.wazipay.data.network.models.transaction.TransactionResponseBody
import com.escrow.wazipay.data.network.models.transaction.TransactionsResponseBody
import com.escrow.wazipay.data.network.models.user.UserDetailsResponseBody
import com.escrow.wazipay.data.network.models.wallet.UserWalletResponseBody
import retrofit2.Response

class ApiRepositoryImpl(private val apiService: ApiService): ApiRepository {
    override suspend fun registerUser(registrationRequestBody: RegistrationRequestBody): Response<RegistrationResponseBody> =
        apiService.registerUser(registrationRequestBody)

    override suspend fun login(loginRequestBody: LoginRequestBody): Response<LoginResponseBody> =
        apiService.login(loginRequestBody)

    override suspend fun setUserPin(setPinRequestBody: SetPinRequestBody): Response<SetPinResponseBody> =
        apiService.setUserPin(setPinRequestBody)

    override suspend fun getUserDetails(
        token: String,
        userId: Int
    ): Response<UserDetailsResponseBody> =
        apiService.getUserDetails(
            token = "Bearer $token",
            userId = userId
        )

    override suspend fun getUserWallet(
        token: String,
        userId: Int
    ): Response<UserWalletResponseBody> =
        apiService.getUserWallet(
            token = "Bearer $token",
            userId = userId
        )

    override suspend fun getOrders(
        token: String,
        query: String?,
        code: String?,
        merchantId: Int?,
        buyerId: Int?,
        courierId: Int?,
        stage: String?,
        startDate: String?,
        endDate: String
    ): Response<OrdersResponseBody> =
        apiService.getOrders(
            token = "Bearer $token",
            query = query,
            code = code,
            merchantId = merchantId,
            buyerId = buyerId,
            courierId = courierId,
            stage = stage,
            startDate = startDate,
            endDate = endDate
        )

    override suspend fun getOrder(
        token: String,
        orderId: Int
    ): Response<OrderResponseBody> =
        apiService.getOrder(
            token = "Bearer $token",
            orderId = orderId
        )

    override suspend fun getInvoices(
        token: String,
        query: String?,
        businessId: Int?,
        buyerId: Int?,
        merchantId: Int?,
        status: String?,
        startDate: String?,
        endDate: String?
    ): Response<InvoicesResponseBody> =
        apiService.getInvoices(
            token = "Bearer $token",
            query = query,
            businessId = businessId,
            buyerId = buyerId,
            merchantId = merchantId,
            status = status,
            startDate = startDate,
            endDate = endDate
        )

    override suspend fun getInvoice(
        token: String,
        invoiceId: Int
    ): Response<InvoiceResponseBody> =
        apiService.getInvoice(
            token = "Bearer $token",
            invoiceId = invoiceId
        )

    override suspend fun getTransactions(
        token: String,
        userId: Int?,
        query: String?,
        transactionCode: String?,
        transactionType: String?,
        startDate: String?,
        endDate: String?
    ): Response<TransactionsResponseBody> =
        apiService.getTransactions(
            token = "Bearer $token",
            userId = userId,
            query = query,
            transactionCode = transactionCode,
            transactionType = transactionType,
            startDate = startDate,
            endDate = endDate
        )

    override suspend fun getTransaction(
        token: String,
        transactionId: Int
    ): Response<TransactionResponseBody> =
        apiService.getTransaction(
            token = "Bearer $token",
            transactionId = transactionId
        )
}