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

interface ApiRepository {
    suspend fun registerUser(
       registrationRequestBody: RegistrationRequestBody
    ): Response<RegistrationResponseBody>

    suspend fun login(
        loginRequestBody: LoginRequestBody
    ): Response<LoginResponseBody>

    suspend fun setUserPin(
       setPinRequestBody: SetPinRequestBody
    ): Response<SetPinResponseBody>

//    Get user details

    suspend fun getUserDetails(
        token: String,
        userId: Int
    ): Response<UserDetailsResponseBody>

//    Get user wallet

    suspend fun getUserWallet(
        token: String,
    ): Response<UserWalletResponseBody>

//    Get orders

    suspend fun getOrders(
        token: String,
        query: String?,
        code: String?,
        merchantId: Int?,
        buyerId: Int?,
        courierId: Int?,
        stage: String?,
        startDate: String?,
        endDate: String?
    ): Response<OrdersResponseBody>

//    Get order

    suspend fun getOrder (
        token: String,
        orderId: Int
    ): Response<OrderResponseBody>

//    Get invoices

    suspend fun getInvoices(
        token: String,
        query: String?,
        businessId: Int?,
        buyerId: Int?,
        merchantId: Int?,
        status: String?,
        startDate: String?,
        endDate: String?
    ): Response<InvoicesResponseBody>

//    Get invoice

    suspend fun getInvoice(
        token: String,
        invoiceId: Int
    ): Response<InvoiceResponseBody>

//    Get transactions

    suspend fun getTransactions(
        token: String,
        userId: Int?,
        query: String?,
        transactionCode: String?,
        transactionType: String?,
        startDate: String?,
        endDate: String?
    ): Response<TransactionsResponseBody>

//    Get transactions

    suspend fun getTransaction(
        token: String,
        transactionId: Int
    ): Response<TransactionResponseBody>
}