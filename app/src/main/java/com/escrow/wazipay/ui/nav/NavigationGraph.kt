package com.escrow.wazipay.ui.nav

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.escrow.wazipay.ui.screens.auth.LoginScreenComposable
import com.escrow.wazipay.ui.screens.auth.LoginScreenDestination
import com.escrow.wazipay.ui.screens.auth.PinScreenComposable
import com.escrow.wazipay.ui.screens.auth.PinScreenDestination
import com.escrow.wazipay.ui.screens.auth.RegistrationScreenComposable
import com.escrow.wazipay.ui.screens.auth.RegistrationScreenDestination
import com.escrow.wazipay.ui.screens.dashboard.DashboardScreenComposable
import com.escrow.wazipay.ui.screens.dashboard.DashboardScreenDestination
import com.escrow.wazipay.ui.screens.users.common.business.businessDetails.BusinessDetailsScreenComposable
import com.escrow.wazipay.ui.screens.users.common.business.businessDetails.BusinessDetailsScreenDestination
import com.escrow.wazipay.ui.screens.users.common.business.businessList.BusinessesScreenComposable
import com.escrow.wazipay.ui.screens.users.common.business.businessList.BusinessesScreenDestination
import com.escrow.wazipay.ui.screens.users.common.invoice.invoiceDetails.InvoiceDetailsScreenComposable
import com.escrow.wazipay.ui.screens.users.common.invoice.invoiceDetails.InvoiceDetailsScreenDestination
import com.escrow.wazipay.ui.screens.users.common.invoice.invoicesList.InvoicesScreenComposable
import com.escrow.wazipay.ui.screens.users.common.invoice.invoicesList.InvoicesScreenDestination
import com.escrow.wazipay.ui.screens.users.common.order.orderCreation.OrderCreationScreenComposable
import com.escrow.wazipay.ui.screens.users.common.order.orderCreation.OrderCreationScreenDestination
import com.escrow.wazipay.ui.screens.users.common.order.orderDetails.OrderDetailsScreenComposable
import com.escrow.wazipay.ui.screens.users.common.order.orderDetails.OrderDetailsScreenDestination
import com.escrow.wazipay.ui.screens.users.common.order.ordersList.OrdersScreenComposable
import com.escrow.wazipay.ui.screens.users.common.order.ordersList.OrdersScreenDestination
import com.escrow.wazipay.ui.screens.users.common.profile.UserAccountOverviewScreenComposable
import com.escrow.wazipay.ui.screens.users.common.profile.UserAccountOverviewScreenDestination
import com.escrow.wazipay.ui.screens.users.common.profile.verification.UserVerificationScreenComposable
import com.escrow.wazipay.ui.screens.users.common.profile.verification.UserVerificationScreenDestination
import com.escrow.wazipay.ui.screens.users.common.transaction.transactionsList.TransactionsScreenComposable
import com.escrow.wazipay.ui.screens.users.common.transaction.transactionsList.TransactionsScreenDestination
import com.escrow.wazipay.ui.screens.users.common.wallet.deposit.DepositScreenComposable
import com.escrow.wazipay.ui.screens.users.common.wallet.deposit.DepositScreenDestination
import com.escrow.wazipay.ui.screens.users.common.wallet.withdrawal.WithdrawalScreenComposable
import com.escrow.wazipay.ui.screens.users.common.wallet.withdrawal.WithdrawalScreenDestination
import com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment.BusinessSelectionScreenComposable
import com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment.BusinessSelectionScreenDestination
import com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment.InvoiceCreationScreenComposable
import com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment.InvoiceCreationScreenDestination
import com.escrow.wazipay.ui.screens.users.specific.merchant.businessAddition.BusinessAdditionScreenComposable
import com.escrow.wazipay.ui.screens.users.specific.merchant.businessAddition.BusinessAdditionScreenDestination
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.CourierAssignmentScreenComposable
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.CourierAssignmentScreenDestination
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.CourierSelectionScreenComposable
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.CourierSelectionScreenDestination
import com.escrow.wazipay.ui.screens.users.specific.merchant.invoiceCreation.BuyerSelectionScreenComposable
import com.escrow.wazipay.ui.screens.users.specific.merchant.invoiceCreation.BuyerSelectionScreenDestination
import com.escrow.wazipay.ui.screens.users.specific.merchant.invoiceCreation.InvoiceIssuanceScreenComposable
import com.escrow.wazipay.ui.screens.users.specific.merchant.invoiceCreation.InvoiceIssuanceScreenDestination
import com.escrow.wazipay.ui.start.SplashScreenComposable
import com.escrow.wazipay.ui.start.SplashScreenDestination

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    darkMode: Boolean,
    onSwitchTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = SplashScreenDestination.route,
        modifier = modifier
    ) {
        composable(route = SplashScreenDestination.route) {
            SplashScreenComposable(
                navigateToLoginScreen = {
                    navController.navigate(LoginScreenDestination.route)
                },
                navigateToLoginScreenWithArgs = { phoneNumber, pin ->
                    navController.navigate("${LoginScreenDestination.route}/${phoneNumber}/${pin}")
                },
                navigateToDashboardScreen = {
                    navController.navigate(DashboardScreenDestination.route)
                },
                navigateToSetPinScreen = {
                    navController.navigate(PinScreenDestination.route)
                }
            )
        }
        composable(route = LoginScreenDestination.route) {
            LoginScreenComposable(
                navigateToRegistrationScreen = {
                    navController.navigate(RegistrationScreenDestination.route)
                },
                navigateToDashboardScreen = {
                    navController.navigate(DashboardScreenDestination.route)
                }
            )
        }
        composable(
            route = LoginScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(LoginScreenDestination.phoneNumber) {
                    type = NavType.StringType
                },
                navArgument(LoginScreenDestination.pin) {
                    type = NavType.StringType
                },
            )
        ) {
            LoginScreenComposable(
                navigateToRegistrationScreen = {
                    navController.navigate(RegistrationScreenDestination.route)
                },
                navigateToDashboardScreen = {
                    navController.navigate(DashboardScreenDestination.route)
                }
            )
        }
        composable(RegistrationScreenDestination.route) {
            RegistrationScreenComposable(
                navigateToLoginScreen = {
                    navController.navigate(LoginScreenDestination.route)
                },
                navigateToSetPinScreen = {
                    navController.navigate(PinScreenDestination.route)
                }
            )
        }
        composable(PinScreenDestination.route) {
            PinScreenComposable(
                navigateToLoginScreenWithArgs = { phoneNumber, pin ->
                    navController.navigate("${LoginScreenDestination.route}/${phoneNumber}/${pin}")
                }
            )
        }
        composable(DashboardScreenDestination.route) {
            DashboardScreenComposable(
                darkMode = darkMode,
                onSwitchTheme = onSwitchTheme,
                navigateToLoginScreenWithArgs = {phoneNumber, pin ->
                    navController.navigate("${LoginScreenDestination.route}/${phoneNumber}/${pin}")
                },
                navigateToDepositScreen = {
                    navController.navigate(DepositScreenDestination.route)
                },
                navigateToWithdrawalScreen = {
                    navController.navigate(WithdrawalScreenDestination.route)
                },
                navigateToBusinessDetailsScreen = {
                    navController.navigate("${BusinessDetailsScreenDestination.route}/${it}")
                },
                navigateToDashboardScreen = {
                    navController.navigate(DashboardScreenDestination.route)
                },
                navigateToBusinessSelectionScreen = {
                    navController.navigate(BusinessSelectionScreenDestination.route)
                },
                navigateToOrderDetailsScreen = { orderId, fromPaymentScreen ->
                    navController.navigate("${OrderDetailsScreenDestination.route}/${orderId}/${fromPaymentScreen}")
                },
                navigateToInvoiceCreationScreen = {
                    navController.navigate("${InvoiceCreationScreenDestination.route}/${it}")
                },
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToOrdersScreen = {
                    navController.navigate(OrdersScreenDestination.route)
                },
                navigateToInvoicesScreen = {
                    navController.navigate(InvoicesScreenDestination.route)
                },
                navigateToTransactionsScreen = {
                    navController.navigate(TransactionsScreenDestination.route)
                },
                navigateToBuyerSelectionScreen = {
                    navController.navigate("${BuyerSelectionScreenDestination.route}/${it}")
                },
                navigateToBusinessSelectionScreenWithArgs = {
                    navController.navigate("${BusinessSelectionScreenDestination.route}/${it}")
                },
                navigateToInvoiceDetailsScreen = {
                    navController.navigate("${InvoiceDetailsScreenDestination.route}/${it}")
                },
                navigateToInvoicesScreenWithStatus = {
                    navController.navigate("${InvoicesScreenDestination.route}/${it}")
                },
                navigateToOrdersScreenWithStatus = {
                    navController.navigate("${OrdersScreenDestination.route}/status/${it}")
                },
                navigateToBusinessAdditionScreen = {
                    navController.navigate(BusinessAdditionScreenDestination.route)
                },
                navigateToBusinessScreenWithOwnerId = {
                    navController.navigate("${BusinessesScreenDestination.route}/${it}")
                },
                navigateToUserVerificationScreen = {
                    navController.navigate(UserVerificationScreenDestination.route)
                },
                navigateToUserAccountOverviewScreen = {
                    navController.navigate(UserAccountOverviewScreenDestination.route)
                }
            )
        }

        composable(
            DashboardScreenDestination.routeWithChild,
            arguments = listOf(
                navArgument(DashboardScreenDestination.child) {
                    type = NavType.StringType
                }
            )
        ) {
            DashboardScreenComposable(
                darkMode = darkMode,
                onSwitchTheme = onSwitchTheme,
                navigateToLoginScreenWithArgs = {phoneNumber, pin ->
                    navController.navigate("${LoginScreenDestination.route}/${phoneNumber}/${pin}")
                },
                navigateToDepositScreen = {
                    navController.navigate(DepositScreenDestination.route)
                },
                navigateToWithdrawalScreen = {
                    navController.navigate(WithdrawalScreenDestination.route)
                },
                navigateToBusinessDetailsScreen = {
                    navController.navigate("${BusinessDetailsScreenDestination.route}/${it}")
                },
                navigateToDashboardScreen = {
                    navController.navigate(DashboardScreenDestination.route)
                },
                navigateToBusinessSelectionScreen = {
                    navController.navigate(BusinessSelectionScreenDestination.route)
                },
                navigateToOrderDetailsScreen = { orderId, fromPaymentScreen ->
                    navController.navigate("${OrderDetailsScreenDestination.route}/${orderId}/${fromPaymentScreen}")
                },
                navigateToInvoiceCreationScreen = {
                    navController.navigate("${InvoiceCreationScreenDestination.route}/${it}")
                },
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToOrdersScreen = {
                    navController.navigate(OrdersScreenDestination.route)
                },
                navigateToInvoicesScreen = {
                    navController.navigate(InvoicesScreenDestination.route)
                },
                navigateToTransactionsScreen = {
                    navController.navigate(TransactionsScreenDestination.route)
                },
                navigateToBuyerSelectionScreen = {
                    navController.navigate("${BuyerSelectionScreenDestination.route}/${it}")
                },
                navigateToBusinessSelectionScreenWithArgs = {
                    navController.navigate("${BusinessSelectionScreenDestination.route}/${it}")
                },
                navigateToInvoiceDetailsScreen = {
                    navController.navigate("${InvoiceDetailsScreenDestination.route}/${it}")
                },
                navigateToInvoicesScreenWithStatus = {
                    navController.navigate("${InvoicesScreenDestination.route}/${it}")
                },
                navigateToOrdersScreenWithStatus = {
                    navController.navigate("${OrdersScreenDestination.route}/status/${it}")
                },
                navigateToBusinessAdditionScreen = {
                    navController.navigate(BusinessAdditionScreenDestination.route)
                },
                navigateToBusinessScreenWithOwnerId = {
                    navController.navigate("${BusinessesScreenDestination.route}/${it}")
                },
                navigateToUserVerificationScreen = {
                    navController.navigate(UserVerificationScreenDestination.route)
                },
                navigateToUserAccountOverviewScreen = {
                    navController.navigate(UserAccountOverviewScreenDestination.route)
                }
            )
        }

        composable(DepositScreenDestination.route) {
            DepositScreenComposable(
                navigateToDashboardScreen = {
                    navController.navigate(DashboardScreenDestination.route)
                },
                navigateToPreviousScreen = {
                    navController.navigateUp()
                }
            )
        }

        composable(WithdrawalScreenDestination.route) {
            WithdrawalScreenComposable(
                navigateToDashboardScreen = {
                    navController.navigate(DashboardScreenDestination.route)
                }
            )
        }

        composable(
            BusinessDetailsScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(BusinessDetailsScreenDestination.businessId) {
                    type = NavType.StringType
                }
            )
        ) {
            BusinessDetailsScreenComposable(
                navigateToOrdersScreenWithArgs = {
                    navController.navigate("${OrdersScreenDestination.route}/${it}")
                },
                navigateToCreateOrderScreenWithArgs = {
                    navController.navigate("${OrderCreationScreenDestination.route}/${it}")
                },
                navigateToPreviousScreen = {
                    navController.navigateUp()
                }
            )
        }
        composable(
            OrdersScreenDestination.route) {
            OrdersScreenComposable(
                navigateToLoginScreenWithArgs = {phoneNumber, pin ->
                    navController.navigate("${LoginScreenDestination.route}/${phoneNumber}/${pin}")
                },
                navigateToOrderDetailsScreen = { orderId, fromPaymentScreen ->
                    navController.navigate("${OrderDetailsScreenDestination.route}/${orderId}/${fromPaymentScreen}")
                },
                navigateToPreviousScreen = {
                    navController.navigateUp()
                }
            )
        }
        composable(
            OrdersScreenDestination.routeWithBusinessId,
            arguments = listOf(
                navArgument(OrdersScreenDestination.businessId) {
                    type = NavType.StringType
                }
            )
        ) {
            OrdersScreenComposable(
                navigateToLoginScreenWithArgs = {phoneNumber, pin ->
                    navController.navigate("${LoginScreenDestination.route}/${phoneNumber}/${pin}")
                },
                navigateToOrderDetailsScreen = { orderId, fromPaymentScreen ->
                    navController.navigate("${OrderDetailsScreenDestination.route}/${orderId}/${fromPaymentScreen}")
                },
                navigateToPreviousScreen = {
                    navController.navigateUp()
                }
            )
        }
        composable(
            OrdersScreenDestination.routeWithStage,
            arguments = listOf(
                navArgument(OrdersScreenDestination.stage) {
                    type = NavType.StringType
                }
            )
        ) {
            OrdersScreenComposable(
                navigateToLoginScreenWithArgs = {phoneNumber, pin ->
                    navController.navigate("${LoginScreenDestination.route}/${phoneNumber}/${pin}")
                },
                navigateToOrderDetailsScreen = { orderId, fromPaymentScreen ->
                    navController.navigate("${OrderDetailsScreenDestination.route}/${orderId}/${fromPaymentScreen}")
                },
                navigateToPreviousScreen = {
                    navController.navigateUp()
                }
            )
        }
        composable(
            OrderCreationScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(OrderCreationScreenDestination.businessId) {
                    type = NavType.StringType
                }
            )
        ) {
            OrderCreationScreenComposable(
                navigateToDashboardWithChildScreen = {child ->
                    navController.navigate("${DashboardScreenDestination.route}/${child}")
                },
                navigateToPreviousScreen = {
                    navController.navigateUp()
                }
            )
        }
        composable(
            DashboardScreenDestination.routeWithChild,
            arguments = listOf(
                navArgument(DashboardScreenDestination.child) {
                    type = NavType.StringType
                }
            )
        ) {
            Log.d("dashboardScreenNavigation", DashboardScreenDestination.routeWithChild)
            DashboardScreenComposable(
                darkMode = darkMode,
                onSwitchTheme = onSwitchTheme,
                navigateToLoginScreenWithArgs = {phoneNumber, pin ->
                    navController.navigate("${LoginScreenDestination.route}/${phoneNumber}/${pin}")
                },
                navigateToDepositScreen = {
                    navController.navigate(DepositScreenDestination.route)
                },
                navigateToWithdrawalScreen = {
                    navController.navigate(WithdrawalScreenDestination.route)
                },
                navigateToBusinessDetailsScreen = {
                    navController.navigate("${BusinessDetailsScreenDestination.route}/${it}")
                },
                navigateToDashboardScreen = {
                    navController.navigate(DashboardScreenDestination.route)
                },
                navigateToBusinessSelectionScreen = {
                    navController.navigate(BusinessSelectionScreenDestination.route)
                },
                navigateToOrderDetailsScreen = { orderId, fromPaymentScreen ->
                    navController.navigate("${OrderDetailsScreenDestination.route}/${orderId}/${fromPaymentScreen}")
                },
                navigateToInvoiceCreationScreen = {
                    navController.navigate("${InvoiceCreationScreenDestination.route}/${it}")
                },
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToOrdersScreen = {
                    navController.navigate(OrdersScreenDestination.route)
                },
                navigateToInvoicesScreen = {
                    navController.navigate(InvoicesScreenDestination.route)
                },
                navigateToTransactionsScreen = {
                    navController.navigate(TransactionsScreenDestination.route)
                },
                navigateToBuyerSelectionScreen = {
                    navController.navigate("${BuyerSelectionScreenDestination.route}/${it}")
                },
                navigateToBusinessSelectionScreenWithArgs = {
                    navController.navigate("${BusinessSelectionScreenDestination.route}/${it}")
                },
                navigateToInvoiceDetailsScreen = {
                    navController.navigate("${InvoiceDetailsScreenDestination.route}/${it}")
                },
                navigateToInvoicesScreenWithStatus = {
                    navController.navigate("${InvoicesScreenDestination.route}/${it}")
                },
                navigateToOrdersScreenWithStatus = {
                    navController.navigate("${OrdersScreenDestination.route}/status/${it}")
                },
                navigateToBusinessAdditionScreen = {
                    navController.navigate(BusinessAdditionScreenDestination.route)
                },
                navigateToBusinessScreenWithOwnerId = {
                    navController.navigate("${BusinessesScreenDestination.route}/${it}")
                },
                navigateToUserVerificationScreen = {
                    navController.navigate(UserVerificationScreenDestination.route)
                },
                navigateToUserAccountOverviewScreen = {
                    navController.navigate(UserAccountOverviewScreenDestination.route)
                }
            )
        }

        composable(BusinessSelectionScreenDestination.route) {
            BusinessSelectionScreenComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToInvoiceCreationScreen = {
                    navController.navigate("${InvoiceCreationScreenDestination.route}/${it}")
                },
                navigateToBuyerSelectionScreen = {
                    navController.navigate("${BuyerSelectionScreenDestination.route}/${it}")
                }
            )
        }

        composable(
            BusinessSelectionScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(BusinessSelectionScreenDestination.toBuyerSelectionScreen) {
                    type = NavType.BoolType
                }
            )
        ) {
            BusinessSelectionScreenComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToInvoiceCreationScreen = {
                    navController.navigate("${InvoiceCreationScreenDestination.route}/${it}")
                },
                navigateToBuyerSelectionScreen = {
                    navController.navigate("${BuyerSelectionScreenDestination.route}/${it}")
                }
            )
        }

        composable(
            InvoiceCreationScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(InvoiceCreationScreenDestination.businessId) {
                    type = NavType.StringType
                }
            )
        ) {
            InvoiceCreationScreenComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToOrderDetailsScreen = { orderId, fromPaymentScreen ->
                    navController.popBackStack()
                    navController.navigate("${OrderDetailsScreenDestination.route}/${orderId}/${fromPaymentScreen}")
                }
            )
        }

        composable(
            OrderDetailsScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(OrderDetailsScreenDestination.orderId) {
                    type = NavType.StringType
                },
                navArgument(OrderDetailsScreenDestination.fromPaymentScreen) {
                    type = NavType.BoolType
                }
            )
        ) {
            OrderDetailsScreenComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToDashboardScreen = {
                    navController.navigate(DashboardScreenDestination.route)
                },
                navigateToCourierSelectionScreen = {orderId ->
                    navController.navigate("${CourierSelectionScreenDestination.route}/${orderId}")
                }
            )
        }

        composable(
            CourierSelectionScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(CourierSelectionScreenDestination.orderId) {
                    type = NavType.StringType
                }
            )
        ) {
            CourierSelectionScreenComposable(
                navigateToCourierAssignmentScreen = {orderId, courierId ->
                    navController.navigate("${CourierAssignmentScreenDestination.route}/${orderId}/${courierId}")
                },
                navigateToPreviousScreen = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            CourierAssignmentScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(CourierAssignmentScreenDestination.orderId) {
                    type = NavType.StringType
                },
                navArgument(CourierAssignmentScreenDestination.courierId) {
                    type = NavType.StringType
                }
            )
        ) {
            CourierAssignmentScreenComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToOrderDetailsScreen = { orderId, fromPaymentScreen ->
                    navController.navigate("${OrderDetailsScreenDestination.route}/${orderId}/${fromPaymentScreen}")
                }
            )
        }
        composable(InvoicesScreenDestination.route) {
            InvoicesScreenComposable(
                navigateToBusinessSelectionScreenWithArgs = {
                    navController.navigate("${BusinessSelectionScreenDestination.route}/${it}")
                },
                navigateToInvoiceDetailsScreen = {
                    navController.navigate("${InvoiceDetailsScreenDestination.route}/${it}")
                },
                navigateToPreviousScreen = {
                    navController.navigateUp()
                }
            )
        }
        composable(
            InvoicesScreenDestination.routeWithStatus,
            arguments = listOf(
                navArgument(InvoicesScreenDestination.status) {
                    type = NavType.StringType
                }
            )
        ) {
            InvoicesScreenComposable(
                navigateToBusinessSelectionScreenWithArgs = {
                    navController.navigate("${BusinessSelectionScreenDestination.route}/${it}")
                },
                navigateToInvoiceDetailsScreen = {
                    navController.navigate("${InvoiceDetailsScreenDestination.route}/${it}")
                },
                navigateToPreviousScreen = {
                    navController.navigateUp()
                }
            )
        }
        composable(TransactionsScreenDestination.route) {
            var filtering by rememberSaveable {
                mutableStateOf(false)
            }
            TransactionsScreenComposable(
                filtering = filtering,
                onFilter = {
                    filtering = !filtering
                },
                navigateToPreviousScreen = {
                    navController.navigateUp()
                }
            )
        }
        
        composable(
            BuyerSelectionScreenDestination.routeWithBusinessId,
            arguments = listOf(
                navArgument(BuyerSelectionScreenDestination.businessId) {
                    type = NavType.StringType
                }
            )
        ) {
            BuyerSelectionScreenComposable(
                navigateToInvoiceIssuanceScreen = {buyerId, businessId ->
                    navController.navigate("${InvoiceIssuanceScreenDestination.route}/${buyerId}/${businessId}")
                },
                navigateToPreviousScreen = { 
                    navController.navigateUp()
                }
            )
        }
        composable(
            InvoiceIssuanceScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(InvoiceIssuanceScreenDestination.buyerId) {
                    type = NavType.StringType
                },
                navArgument(InvoiceIssuanceScreenDestination.businessId) {
                    type = NavType.StringType
                },
            )
        ) {
            InvoiceIssuanceScreenComposable(
                navigateToInvoiceDetailsScreen = {
                    navController.popBackStack()
                    navController.navigate("${InvoiceDetailsScreenDestination.route}/${it}")
                },
                navigateToPreviousScreen = { navController.navigateUp() }
            )
        }
        composable(
            InvoiceDetailsScreenDestination.routeWithInvoiceId,
            arguments = listOf(
                navArgument(InvoiceDetailsScreenDestination.invoiceId) {
                    type = NavType.StringType
                }
            )
        ) {
            InvoiceDetailsScreenComposable(
                navigateToOrderDetailsScreen = { orderId, fromPaymentScreen ->
                    navController.navigate("${OrderDetailsScreenDestination.route}/${orderId}/${fromPaymentScreen}")
                },
                navigateToPreviousScreen = {
                    navController.navigateUp()
                }
            )
        }
        composable(BusinessAdditionScreenDestination.route) {
            BusinessAdditionScreenComposable(
                navigateToBusinessDetailsScreen = {
                    navController.popBackStack()
                    navController.navigate("${BusinessDetailsScreenDestination.route}/${it}")
                },
                navigateToPreviousScreen = {
                    navController.navigateUp()
                }
            )
        }
        composable(
            BusinessesScreenDestination.routeWithOwnerId,
            arguments = listOf(
                navArgument(BusinessesScreenDestination.ownerId) {
                    type = NavType.StringType
                }
            )
        ) {
            BusinessesScreenComposable(
                navigateToBusinessDetailsScreen = {
                    navController.navigate("${BusinessDetailsScreenDestination.route}/${it}")
                },
                navigateToBusinessAdditionScreen = {
                    navController.navigate(BusinessAdditionScreenDestination.route)
                },
                navigateToPreviousScreen = {
                    navController.navigateUp()
                }
            )
        }
        composable(UserVerificationScreenDestination.route) {
            UserVerificationScreenComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                }
            )
        }
        composable(UserAccountOverviewScreenDestination.route) {
            UserAccountOverviewScreenComposable(
                navigateToLoginScreenWithArgs = {phoneNumber, pin ->
                    navController.navigate("${LoginScreenDestination.route}/${phoneNumber}/${pin}")
                },
                navigateToUserVerificationScreen = {
                    navController.navigate(UserVerificationScreenDestination.route)
                },
                navigateToPreviousScreen = {
                    navController.navigateUp()
                }
            )
        }
    }
}