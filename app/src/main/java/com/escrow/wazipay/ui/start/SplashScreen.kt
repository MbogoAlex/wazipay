package com.escrow.wazipay.ui.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.R
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenWidth
import kotlinx.coroutines.delay

object SplashScreenDestination: AppNavigation {
    override val title: String = "Splash screen"
    override val route: String = "splash-screen"
}

@Composable
fun SplashScreenComposable(
    navigateToLoginScreen: () -> Unit,
    navigateToLoginScreenWithArgs: (phoneNumber: String, pin: String) -> Unit,
    navigateToDashboardScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: SplashViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        delay(2000)
        if(!uiState.isLoading && uiState.isNavigating) {
            if(uiState.userDetails.username == null) {
                if(uiState.userDetails.phoneNumber != null && uiState.userDetails.pin != null) {
                    navigateToLoginScreenWithArgs(uiState.userDetails.phoneNumber!!, uiState.userDetails.pin!!)
                } else {
                    navigateToLoginScreen()
                }
            } else {
                navigateToDashboardScreen()
            }
            viewModel.stopNavigating()
        }
    }

    Box {
        SplashScreen()
    }
}

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background,)
            .fillMaxSize()
    ) {
        Icon(
            tint = MaterialTheme.colorScheme.onBackground,
            painter = painterResource(id = R.drawable.wazipay_logo),
            contentDescription = null,
            modifier = Modifier
                .size(screenWidth(x = 150.0))
                .clip(CircleShape)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    WazipayTheme {
        SplashScreen()
    }
}
