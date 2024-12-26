package com.escrow.wazipay.ui.nav

import android.os.Build
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
import com.escrow.wazipay.ui.dashboard.DashboardScreenComposable
import com.escrow.wazipay.ui.dashboard.DashboardScreenDestination
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
                }
            )
        }
    }
}