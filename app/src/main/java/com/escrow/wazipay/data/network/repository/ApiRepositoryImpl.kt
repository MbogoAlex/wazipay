package com.escrow.wazipay.data.network.repository

import com.escrow.wazipay.data.network.models.business.BusinessResponseBody
import com.escrow.wazipay.data.network.models.business.BusinessesResponseBody
import com.escrow.wazipay.data.network.models.common.LoginRequestBody
import com.escrow.wazipay.data.network.models.common.LoginResponseBody
import com.escrow.wazipay.data.network.models.common.RegistrationRequestBody
import com.escrow.wazipay.data.network.models.common.RegistrationResponseBody
import com.escrow.wazipay.data.network.models.common.SetPinRequestBody
import com.escrow.wazipay.data.network.models.common.SetPinResponseBody
import com.escrow.wazipay.data.network.models.invoice.InvoiceResponseBody
import com.escrow.wazipay.data.network.models.invoice.InvoicesResponseBody
import com.escrow.wazipay.data.network.models.order.OrderCreationRequestBody
import com.escrow.wazipay.data.network.models.order.OrderCreationResponseBody
import com.escrow.wazipay.data.network.models.order.OrderResponseBody
import com.escrow.wazipay.data.network.models.order.OrdersResponseBody
import com.escrow.wazipay.data.network.models.transaction.TransactionResponseBody
import com.escrow.wazipay.data.network.models.transaction.TransactionsResponseBody
import com.escrow.wazipay.data.network.models.user.UserDetailsResponseBody
import com.escrow.wazipay.data.network.models.wallet.DepositRequestBody
import com.escrow.wazipay.data.network.models.wallet.UserWalletResponseBody
import com.escrow.wazipay.data.network.models.wallet.WithdrawalRequestBody
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
        token: String
    ): Response<UserWalletResponseBody> =
        apiService.getUserWallet(
            token = "Bearer $token"
        )

    override suspend fun getOrders(
        token: String,
        query: String?,
        code: String?,
        merchantId: Int?,
        buyerId: Int?,
        courierId: Int?,
        businessId: Int?,
        stage: String?,
        startDate: String?,
        endDate: String?
    ): Response<OrdersResponseBody> =
        apiService.getOrders(
            token = "Bearer $token",
            query = query,
            code = code,
            merchantId = merchantId,
            buyerId = buyerId,
            courierId = courierId,
            businessId = businessId,
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

    override suspend fun deposit(
        token: String,
        depositRequestBody: DepositRequestBody
    ): Response<UserWalletResponseBody> =
        apiService.deposit(
            token = "Bearer $token",
            depositRequestBody = depositRequestBody
        )

    override suspend fun withdraw(
        token: String,
        withdrawalRequestBody: WithdrawalRequestBody
    ): Response<UserWalletResponseBody> =
        apiService.withdraw(
            token = "Bearer $token",
            withdrawalRequestBody = withdrawalRequestBody
        )

    override suspend fun getBusinesses(
        token: String,
        query: String?,
        ownerId: Int?,
        archived: Boolean?,
        startDate: String?,
        endDate: String?
    ): Response<BusinessesResponseBody> =
        apiService.getBusinesses(
            token = "Bearer $token",
            query = query,
            ownerId = ownerId,
            archived = archived,
            startDate = startDate,
            endDate = endDate
        )

    override suspend fun getBusiness(
        token: String,
        businessId: Int
    ): Response<BusinessResponseBody> =
        apiService.getBusiness(
            token = "Bearer $token",
            businessId = businessId
        )

    override suspend fun createOrder(
        token: String,
        orderCreationRequestBody: OrderCreationRequestBody
    ): Response<OrderCreationResponseBody> =
        apiService.createOrder(
            token = "Bearer $token",
            orderCreationRequestBody = orderCreationRequestBody
        )

    override suspend fun getUsers(
        token: String,
        query: String?,
        verificationStatus: String?,
        startDate: String?,
        endDate: String?
    ): Response<UserDetailsResponseBody> =
        apiService.getUsers(
            token = token,
            query = query,
            verificationStatus = verificationStatus,
            startDate = startDate,
            endDate = endDate
        )
}