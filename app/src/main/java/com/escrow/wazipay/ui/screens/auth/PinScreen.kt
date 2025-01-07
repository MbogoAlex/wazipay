package com.escrow.wazipay.ui.screens.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

object PinScreenDestination: AppNavigation {
    override val title: String = "Pin screen"
    override val route: String = "pin-screen"

}
@Composable
fun PinScreenComposable(
    navigateToLoginScreenWithArgs: (phoneNumber: String, pin: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val viewModel: PinViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    when(uiState.pinSetStatus) {
        PinSetStatus.INITIAL -> {}
        PinSetStatus.LOADING -> {}
        PinSetStatus.SUCCESS -> {
            viewModel.resetStatus()
            Toast.makeText(context, uiState.pinSetMessage, Toast.LENGTH_SHORT).show()
            navigateToLoginScreenWithArgs(uiState.userDetails.phoneNumber!!, uiState.pin)
        }
        PinSetStatus.FAIL -> {
            viewModel.resetStatus()
            Toast.makeText(context, uiState.pinSetMessage, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        PinScreen(
            context = context,
            pin = uiState.pin,
            onChangePin = {
                viewModel.updatePin(it)
                viewModel.enableButton()
            },
            onSetPin = viewModel::setPin,
            buttonEnabled = uiState.buttonEnabled && uiState.pinSetStatus != PinSetStatus.LOADING,
            pinSetStatus = uiState.pinSetStatus
        )
    }
}


@Composable
fun PinScreen(
    context: Context?,
    pin: String,
    onChangePin: (pin: String) -> Unit,
    onSetPin: () -> Unit,
    buttonEnabled: Boolean,
    pinSetStatus: PinSetStatus,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = screenHeight(x = 16.0),
                horizontal = screenWidth(x = 16.0)
            )
    ) {
        Text(
            text = "Set User Pin",
            fontSize = screenFontSize(x = 22.0).sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 24.0)))
        PinTextField(
            pinText = pin,
            onPinChange = { value, _ ->
                if(pin.length > 6) {
                    Toast.makeText(context!!, "Pin cannot exceed 6 digits", Toast.LENGTH_SHORT).show()
                } else {
                    onChangePin(value)
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 24.0)))
        Text(
            text = "You will use this PIN during login",
            fontSize = screenFontSize(x = 14.0).sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            enabled = buttonEnabled,
            onClick = onSetPin,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if(pinSetStatus == PinSetStatus.LOADING) {
                Text(
                    text = "Loading...",
                    fontSize = screenFontSize(x = 14.0).sp,
                )
            } else {
                Text(
                    text = "Set Pin",
                    fontSize = screenFontSize(x = 14.0).sp,
                )
            }
        }
    }
}

@Composable
fun PinTextField(
    modifier: Modifier = Modifier,
    pinText: String,
    pinCount: Int = 6,
    onPinChange: (String, Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        if (pinText.length > pinCount) {
            throw IllegalArgumentException("Pin text value must not have more than otpCount: $pinCount characters")
        }
    }

    BasicTextField(
        modifier = modifier,
        value = TextFieldValue(pinText, selection = TextRange(pinText.length)),
        onValueChange = {
            if (it.text.length <= pinCount) {
                onPinChange.invoke(it.text, it.text.length == pinCount)
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.NumberPassword
        ),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(pinCount) { index ->
                    CharView(
                        index = index,
                        text = pinText
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                }
            }
        }
    )
}

@Composable
private fun CharView(
    index: Int,
    text: String
) {
    val isFocused = text.length == index
    val char = when {
        index == text.length -> ""
        index > text.length -> ""
        else -> text[index].toString()
    }
    Text(
        modifier = Modifier
            .size(screenWidth(x = 40.0))
            .border(
                1.dp, when {
                    isFocused -> Color.DarkGray
                    else -> Color.LightGray
                }, RoundedCornerShape(screenWidth(x = 8.0))
            )
            .padding(2.dp),
        text = char,
//        style = MaterialTheme.typography.labelSmall,
        color = if (isFocused) {
            Color.LightGray
        } else {
            Color.DarkGray
        },
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PinScreenPreview() {
    WazipayTheme {
        PinScreen(
            context = null,
            pin = "",
            onChangePin = {},
            onSetPin = { /*TODO*/ },
            buttonEnabled = false,
            pinSetStatus = PinSetStatus.INITIAL
        )
    }
}