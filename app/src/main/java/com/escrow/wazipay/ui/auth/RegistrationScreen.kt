package com.escrow.wazipay.ui.auth

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.escrow.wazipay.R
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.composables.TextFieldComposable
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

@Composable
fun RegistrationScreenComposable(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        RegistrationScreen()
    }
}

@Composable
fun RegistrationScreen(
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
            value = "",
            leadingIcon = R.drawable.person,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        TextFieldComposable(
            shape = RoundedCornerShape(screenWidth(x = 20.0)),
            label = "Phone number",
            value = "",
            leadingIcon = R.drawable.phone,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        TextFieldComposable(
            shape = RoundedCornerShape(screenWidth(x = 20.0)),
            label = "Password",
            value = "",
            leadingIcon = R.drawable.password,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        TextFieldComposable(
            shape = RoundedCornerShape(screenWidth(x = 20.0)),
            label = "Confirm Password",
            value = "",
            leadingIcon = R.drawable.password,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = {},
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
            TextButton(onClick = { /*TODO*/ }) {
                Text(
                    text = "Login",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Sign up")
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistrationScreenPreview() {
    WazipayTheme {
        RegistrationScreen(
            modifier = Modifier
                .safeDrawingPadding()
        )
    }
}