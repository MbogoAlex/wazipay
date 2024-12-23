package com.escrow.wazipay.ui.auth

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.R
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.composables.TextFieldComposable
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

object RegistrationScreenDestination: AppNavigation {
    override val title: String = "Registration screen"
    override val route: String = "registration-screen"
}

@Composable
fun RegistrationScreenComposable(
    navigateToLoginScreen: () -> Unit,
    navigateToSetPinScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    BackHandler(onBack = navigateToLoginScreen)

    val viewModel: RegistrationViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    when(uiState.registrationStatus) {
        RegistrationStatus.INITIAL -> {}
        RegistrationStatus.LOADING -> {}
        RegistrationStatus.SUCCESS -> {
            viewModel.resetStatus()
            Toast.makeText(context, uiState.registrationMessage, Toast.LENGTH_LONG).show()
            navigateToSetPinScreen()
        }
        RegistrationStatus.FAIL -> {
            viewModel.resetStatus()
            Toast.makeText(context, uiState.registrationMessage, Toast.LENGTH_LONG).show()
        }
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        RegistrationScreen(
            username = uiState.username,
            phoneNumber = uiState.phoneNumber,
            email = uiState.email,
            onChangeUsername = {
                viewModel.updateUsername(it)
                viewModel.enableButton()
            },
            onChangePhoneNumber = {
                viewModel.updatePhoneNumber(it)
                viewModel.enableButton()
            },
            onChangeEmail = {
                viewModel.updateEmail(it)
                viewModel.enableButton()
            },
            registrationStatus = uiState.registrationStatus,
            buttonEnabled = uiState.buttonEnabled && uiState.registrationStatus != RegistrationStatus.LOADING,
            onRegister = {
                viewModel.registerUser()
            },
            navigateToLoginScreen = navigateToLoginScreen
        )
    }
}

@Composable
fun RegistrationScreen(
    username: String,
    phoneNumber: String,
    email: String,
    onChangeUsername: (name: String) -> Unit,
    onChangePhoneNumber: (phoneNumber: String) -> Unit,
    onChangeEmail: (email: String) -> Unit,
    registrationStatus: RegistrationStatus,
    buttonEnabled: Boolean,
    onRegister: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = screenWidth(x = 16.0),
                vertical = screenHeight(x = 16.0)
            )
    ) {
        Text(
            text = "Sign up",
            fontWeight = FontWeight.Bold,
            fontSize = screenFontSize(x = 24.0).sp
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = "Create your account",
            fontSize = screenFontSize(x = 14.0).sp
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        TextFieldComposable(
            shape = RoundedCornerShape(screenWidth(x = 20.0)),
            label = "Username",
            value = username,
            leadingIcon = R.drawable.person,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onChangeUsername,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        TextFieldComposable(
            shape = RoundedCornerShape(screenWidth(x = 20.0)),
            label = "Phone number",
            value = phoneNumber,
            leadingIcon = R.drawable.phone,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onChangePhoneNumber,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        TextFieldComposable(
            shape = RoundedCornerShape(screenWidth(x = 20.0)),
            label = "Email",
            value = email,
            leadingIcon = R.drawable.email,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Email
            ),
            onValueChange = onChangeEmail,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account?",
                fontSize = screenFontSize(x = 14.0).sp
            )
            TextButton(onClick = navigateToLoginScreen) {
                Text(
                    text = "Login",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            enabled = buttonEnabled,
            onClick = onRegister,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if(registrationStatus == RegistrationStatus.LOADING) {
                Text(
                    text = "Loading...",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            } else {
                Text(
                    text = "Sign up",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistrationScreenPreview() {
    WazipayTheme {
        RegistrationScreen(
            username = "",
            phoneNumber = "",
            email = "",
            onChangeUsername = {},
            onChangePhoneNumber = {},
            onChangeEmail = {},
            registrationStatus = RegistrationStatus.INITIAL,
            buttonEnabled = false,
            onRegister = {},
            navigateToLoginScreen = {}
        )
    }
}