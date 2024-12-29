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
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/register")
    suspend fun registerUser(
        @Body registrationRequestBody: RegistrationRequestBody
    ): Response<RegistrationResponseBody>

    @POST("auth/login")
    suspend fun login(
        @Body loginRequestBody: LoginRequestBody
    ): Response<LoginResponseBody>

    @PUT("auth/pin")
    suspend fun setUserPin(
        @Body setPinRequestBody: SetPinRequestBody
    ): Response<SetPinResponseBody>

    //    Get user details

    @GET("user/id/{id}")
    suspend fun getUserDetails(
        @Header("Authorization") token: String,
        @Path("id") userId: Int
    ): Response<UserDetailsResponseBody>

//    Get user wallet

    @GET("user/user-wallet")
    suspend fun getUserWallet(
        @Header("Authorization") token: String
    ): Response<UserWalletResponseBody>

//    Get orders

    @GET("user/order")
    suspend fun getOrders(
        @Header("Authorization") token: String,
        @Query("query") query: String?,
        @Query("code") code: String?,
        @Query("merchantId") merchantId: Int?,
        @Query("buyerId") buyerId: Int?,
        @Query("courierId") courierId: Int?,
        @Query("businessId") businessId: Int?,
        @Query("stage") stage: String?,
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?
    ): Response<OrdersResponseBody>

//    Get order

    @GET("user/order/{id}")
    suspend fun getOrder (
        @Header("Authorization") token: String,
        @Path("id") orderId: Int
    ): Response<OrderResponseBody>

//    Get invoices

    @GET("user/invoice")
    suspend fun getInvoices(
        @Header("Authorization") token: String,
        @Query("query") query: String?,
        @Query("businessId") businessId: Int?,
        @Query("buyerId") buyerId: Int?,
        @Query("merchantId") merchantId: Int?,
        @Query("status") status: String?,
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?
    ): Response<InvoicesResponseBody>

//    Get invoice

    @GET("user/invoice/{id}")
    suspend fun getInvoice(
        @Header("Authorization") token: String,
        @Path("id") invoiceId: Int
    ): Response<InvoiceResponseBody>

//    Get transactions

    @GET("user/transaction")
    suspend fun getTransactions(
        @Header("Authorization") token: String,
        @Query("userId") userId: Int?,
        @Query("query") query: String?,
        @Query("transactionCode") transactionCode: String?,
        @Query("transactionType") transactionType: String?,
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?
    ): Response<TransactionsResponseBody>

//    Get transactions

    @GET("user/transaction/{id}")
    suspend fun getTransaction(
        @Header("Authorization") token: String,
        @Path("id") transactionId: Int
    ): Response<TransactionResponseBody>

//    Deposit to user wallet
    @POST("user/deposit")
    suspend fun deposit(
        @Header("Authorization") token: String,
        @Body depositRequestBody: DepositRequestBody,
    ): Response<UserWalletResponseBody>

//    Withdraw from user wallet
    @POST("user/withdrawal")
    suspend fun withdraw(
        @Header("Authorization") token: String,
        @Body withdrawalRequestBody: WithdrawalRequestBody
    ): Response<UserWalletResponseBody>

//    Get businesses
    @GET("user/business")
    suspend fun getBusinesses(
        @Header("Authorization") token: String,
        @Query("query") query: String?,
        @Query("ownerId") ownerId: Int?,
        @Query("archived") archived: Boolean?,
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?
    ): Response<BusinessesResponseBody>

//    Get business
    @GET("user/business/{id}")
    suspend fun getBusiness(
    @Header("Authorization") token: String,
    @Path("id") businessId: Int
    ): Response<BusinessResponseBody>

//    Create order
    @POST("buyer/create-order")
    suspend fun createOrder(
    @Header("Authorization")token: String,
    @Body orderCreationRequestBody: OrderCreationRequestBody
    ): Response<OrderCreationResponseBody>
}