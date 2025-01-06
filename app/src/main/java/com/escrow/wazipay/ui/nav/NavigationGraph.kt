package com.escrow.wazipay.ui.nav

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.escrow.wazipay.ui.auth.LoginScreenComposable
import com.escrow.wazipay.ui.auth.LoginScreenDestination
import com.escrow.wazipay.ui.auth.PinScreenComposable
import com.escrow.wazipay.ui.auth.PinScreenDestination
import com.escrow.wazipay.ui.auth.RegistrationScreenComposable
import com.escrow.wazipay.ui.auth.RegistrationScreenDestination
import com.escrow.wazipay.ui.buyer.invoice.BusinessSelectionScreenComposable
import com.escrow.wazipay.ui.buyer.invoice.BusinessSelectionScreenDestination
import com.escrow.wazipay.ui.buyer.invoice.InvoiceCreationScreenComposable
import com.escrow.wazipay.ui.buyer.invoice.InvoiceCreationScreenDestination
import com.escrow.wazipay.ui.dashboard.DashboardScreenComposable
import com.escrow.wazipay.ui.dashboard.DashboardScreenDestination
import com.escrow.wazipay.ui.general.business.BusinessDetailsScreenComposable
import com.escrow.wazipay.ui.general.business.BusinessDetailsScreenDestination
import com.escrow.wazipay.ui.general.order.OrderCreationScreenComposable
import com.escrow.wazipay.ui.general.order.OrderCreationScreenDestination
import com.escrow.wazipay.ui.general.order.OrderDetailsScreenComposable
import com.escrow.wazipay.ui.general.order.OrderDetailsScreenDestination
import com.escrow.wazipay.ui.general.order.OrdersScreenComposable
import com.escrow.wazipay.ui.general.order.OrdersScreenDestination
import com.escrow.wazipay.ui.general.wallet.deposit.DepositScreenComposable
import com.escrow.wazipay.ui.general.wallet.deposit.DepositScreenDestination
import com.escrow.wazipay.ui.general.wallet.withdrawal.WithdrawalScreenComposable
import com.escrow.wazipay.ui.general.wallet.withdrawal.WithdrawalScreenDestination
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
                }
            )
        }

        composable(DepositScreenDestination.route) {
            DepositScreenComposable(
                navigateToDashboardScreen = {
                    navController.navigate(DashboardScreenDestination.route)
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
            OrdersScreenDestination.routeWithArgs,
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
                }
            )
        }

        composable(BusinessSelectionScreenDestination.route) {
            BusinessSelectionScreenComposable(
                navigateToPreviousScreen = {
                    navController.navigateUp()
                },
                navigateToInvoiceCreationScreen = {
                    navController.popBackStack()
                    navController.navigate("${InvoiceCreationScreenDestination.route}/${it}")
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
                }
            )
        }
    }
}