package com.escrow.wazipay.ui.screens.users.common.wallet.deposit

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.escrow.wazipay.utils.formatMoneyValue
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

object DepositScreenDestination: AppNavigation {
    override val title: String = "Deposit screen"
    override val route: String = "deposit-screen"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DepositScreenComposable(
    navigateToDashboardScreen: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val viewModel: DepositViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    BackHandler(onBack = {
        if(uiState.depositStatus != DepositStatus.LOADING) {
            navigateToDashboardScreen()
        }
    })

    var showConfirmDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var showSuccessDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if(uiState.depositStatus == DepositStatus.SUCCESS) {
        showSuccessDialog = true
    }

    if(showConfirmDialog) {
        DepositConfirmDialog(
            onConfirm = {
                showConfirmDialog = !showConfirmDialog
                viewModel.deposit()
            },
            onDismiss = {
                showConfirmDialog = !showConfirmDialog
            },
            depositAmount = formatMoneyValue(uiState.amount.toDouble())
        )
    }

    if(showSuccessDialog) {
        DepositSuccessDialog(
            onConfirm = {
                viewModel.resetStatus()
                navigateToDashboardScreen()
            },
            onDismiss = {
                viewModel.resetStatus()
                viewModel.getUserWallet()
                showSuccessDialog = !showSuccessDialog
                viewModel.updateAmount("")
                viewModel.enableButton()
            },
            depositAmount = formatMoneyValue(uiState.amount.toDouble()),
            newBalance = formatMoneyValue(uiState.newBalance)
        )
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        DepositScreen(
            walletBalance = formatMoneyValue(uiState.userWalletData.balance),
            depositAmount = uiState.amount,
            onChangeAmount = { value ->
                val validAmount = value.filter { it.isDigit() }
                viewModel.updateAmount(validAmount)
                viewModel.enableButton()
            },
            phoneNumber = uiState.phoneNumber.ifEmpty { uiState.userDetails.phoneNumber ?: "" },
            onChangePhoneNumber = viewModel::onChangePhoneNumber,
            depositStatus = uiState.depositStatus,
            buttonEnabled = uiState.buttonEnabled,
            navigateToPreviousScreen = navigateToPreviousScreen,
            onDeposit = {
                showConfirmDialog = !showConfirmDialog
            }
        )
    }
}

@Composable
fun DepositScreen(
    walletBalance: String,
    depositAmount: String,
    onChangeAmount: (amount: String) -> Unit,
    phoneNumber: String,
    onChangePhoneNumber: (phone: String) -> Unit,
    depositStatus: DepositStatus,
    buttonEnabled: Boolean,
    navigateToPreviousScreen: () -> Unit,
    onDeposit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = screenWidth(x = 16.0),
                vertical = screenHeight(x = 16.0)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                enabled = depositStatus != DepositStatus.LOADING,
                onClick = navigateToPreviousScreen
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.onBackground,
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
            Text(
                text = "Create deposit",
                fontSize = screenFontSize(x = 18.0).sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Text(
            text = "A deposit is money moved to your Wazipay wallet so that you can transact on Wazipay.",
            fontSize = screenFontSize(x = 14.0).sp,
            color = MaterialTheme.colorScheme.onBackground
        )
//        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
//        Image(
//            painter = painterResource(id = R.drawable.wazipay_logo),
//            contentDescription = null,
//            modifier = Modifier
//                .size(screenWidth(x = 150.0))
//                .align(Alignment.CenterHorizontally)
//        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = "How much would you like to deposit?",
            fontSize = screenFontSize(x = 16.0).sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        TextFieldComposable(
            shape = RoundedCornerShape(screenWidth(x = 10.0)),
            label = "Enter deposit amount",
            value = depositAmount,
            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.NumberPassword
            ),
            onValueChange = onChangeAmount,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        TextFieldComposable(
            shape = RoundedCornerShape(screenWidth(x = 10.0)),
            label = "Enter M-PESA phone number",
            value = phoneNumber,
            leadingIcon = R.drawable.phone,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.NumberPassword
            ),
            onValueChange = onChangePhoneNumber,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Current balance: ",
                fontSize = screenFontSize(x = 14.0).sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = walletBalance,
                fontSize = screenFontSize(x = 14.0).sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            enabled = buttonEnabled && depositStatus != DepositStatus.LOADING,
            onClick = onDeposit,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if(depositStatus == DepositStatus.LOADING) {
                Text(
                    text = "Depositing...",
                    fontSize = screenFontSize(x = 14.0).sp,
                )
            } else {
                Text(
                    text = "Create deposit",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }

        }
    }
}

@Composable
fun DepositConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    depositAmount: String,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Confirm deposit",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Are you sure you want to deposit $depositAmount?",
                fontSize = screenFontSize(x = 14.0).sp
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(
                    text = "Confirm",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    )
}

@Composable
fun DepositSuccessDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    depositAmount: String,
    newBalance: String,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Deposit successful",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "You have successfully deposited $depositAmount into your wallet.",
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Text(
                    text = "Your new balance is $newBalance.",
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(
                    text = "Done",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Deposit again",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DepositScreenPreview() {
    WazipayTheme {
        DepositScreen(
            walletBalance = "Ksh1,500",
            depositAmount = "500",
            phoneNumber = "0794649026",
            onChangeAmount = {},
            onChangePhoneNumber = {},
            depositStatus = DepositStatus.INITIAL,
            buttonEnabled = false,
            navigateToPreviousScreen = { /*TODO*/ },
            onDeposit = { /*TODO*/ }
        )
    }
}