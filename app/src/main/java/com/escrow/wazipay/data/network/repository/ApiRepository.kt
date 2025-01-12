package com.escrow.wazipay.data.network.repository

import com.escrow.wazipay.data.network.models.business.BusinessAdditionRequestBody
import com.escrow.wazipay.data.network.models.business.BusinessResponseBody
import com.escrow.wazipay.data.network.models.business.BusinessesResponseBody
import com.escrow.wazipay.data.network.models.common.LoginRequestBody
import com.escrow.wazipay.data.network.models.common.LoginResponseBody
import com.escrow.wazipay.data.network.models.common.RegistrationRequestBody
import com.escrow.wazipay.data.network.models.common.RegistrationResponseBody
import com.escrow.wazipay.data.network.models.common.SetPinRequestBody
import com.escrow.wazipay.data.network.models.common.SetPinResponseBody
import com.escrow.wazipay.data.network.models.courier.CourierAssignmentRequestBody
import com.escrow.wazipay.data.network.models.courier.CourierAssignmentResponseBody
import com.escrow.wazipay.data.network.models.invoice.InvoiceCreationRequestBody
import com.escrow.wazipay.data.network.models.invoice.InvoiceResponseBody
import com.escrow.wazipay.data.network.models.invoice.InvoicesResponseBody
import com.escrow.wazipay.data.network.models.order.OrderCreationRequestBody
import com.escrow.wazipay.data.network.models.order.OrderCreationResponseBody
import com.escrow.wazipay.data.network.models.order.OrderResponseBody
import com.escrow.wazipay.data.network.models.order.OrdersResponseBody
import com.escrow.wazipay.data.network.models.transaction.TransactionResponseBody
import com.escrow.wazipay.data.network.models.transaction.TransactionsResponseBody
import com.escrow.wazipay.data.network.models.user.UserDetailsResponseBody
import com.escrow.wazipay.data.network.models.user.UsersDetailsResponseBody
import com.escrow.wazipay.data.network.models.wallet.DepositRequestBody
import com.escrow.wazipay.data.network.models.wallet.UserWalletResponseBody
import com.escrow.wazipay.data.network.models.wallet.WithdrawalRequestBody
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
        businessId: Int?,
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

    //    Deposit to user wallet
    suspend fun deposit(
        token: String,
        depositRequestBody: DepositRequestBody,
    ): Response<UserWalletResponseBody>

    //    Withdraw from user wallet
    suspend fun withdraw(
        token: String,
        withdrawalRequestBody: WithdrawalRequestBody
    ): Response<UserWalletResponseBody>

    //    Get businesses
    suspend fun getBusinesses(
        token: String,
        query: String?,
        ownerId: Int?,
        archived: Boolean?,
        startDate: String?,
        endDate: String?
    ): Response<BusinessesResponseBody>

    //    Get business
    suspend fun getBusiness(
        token: String,
        businessId: Int
    ): Response<BusinessResponseBody>

    //    Create order
    suspend fun createOrder(
        token: String,
        orderCreationRequestBody: OrderCreationRequestBody
    ): Response<OrderCreationResponseBody>

    //    Get users
    suspend fun getUsers(
        token: String,
        query: String?,
        verificationStatus: String?,
        startDate: String?,
        endDate: String?
    ): Response<UsersDetailsResponseBody>

    //    Get user
    suspend fun getUser(
        token: String,
        userId: Int
    ): Response<UserDetailsResponseBody>

    suspend fun createInvoice(
        token: String,
        invoiceCreationRequestBody: InvoiceCreationRequestBody
    ): Response<InvoiceResponseBody>

    //    Pay invoice
    suspend fun payInvoice(
        token: String,
        invoiceId: Int
    ): Response<InvoiceResponseBody>

    //    Assign courier
    suspend fun assignCourier(
        token: String,
        courierAssignmentRequestBody: CourierAssignmentRequestBody
    ): Response<CourierAssignmentResponseBody>

    //    Add business
    suspend fun addBusiness(
        token: String,
        businessAdditionRequestBody: BusinessAdditionRequestBody
    ): Response<BusinessResponseBody>

    //    Complete delivery
    suspend fun completeDelivery(
        token: String,
        orderId: Int,
    ): Response<OrderResponseBody>
}