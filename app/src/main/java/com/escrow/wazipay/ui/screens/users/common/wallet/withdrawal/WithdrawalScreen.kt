package com.escrow.wazipay.ui.screens.users.common.wallet.withdrawal

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.R
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.composables.TextFieldComposable
import com.escrow.wazipay.utils.formatMoneyValue
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth


object WithdrawalScreenDestination: AppNavigation {
    override val title: String = "Withdrawal screen"
    override val route: String = "withdrawal-screen"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WithdrawalScreenComposable(
    navigateToDashboardScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val viewModel: WithdrawalViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    BackHandler(onBack = {
        if(uiState.withdrawalStatus != WithdrawalStatus.LOADING) {
            navigateToDashboardScreen()
        }
    })

    var showConfirmDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var showSuccessDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if(uiState.withdrawalStatus == WithdrawalStatus.SUCCESS) {
        showSuccessDialog = true
    }
    
    if(showConfirmDialog) {
        WithdrawalConfirmDialog(
            onConfirm = { 
                showConfirmDialog = !showConfirmDialog
                viewModel.withdraw()
            },
            onDismiss = { 
                showConfirmDialog = !showConfirmDialog
            },
            withdrawalAmount = formatMoneyValue(uiState.withdrawalAmount.toDouble()),
            phoneNumber = uiState.phoneNumber.ifEmpty { uiState.userDetails.phoneNumber ?: "" }
        )
    }
    
    if(showSuccessDialog) {
        WithdrawalSuccessDialog(
            onConfirm = {
                viewModel.resetStatus()
                navigateToDashboardScreen()
            },
            onDismiss = {
                viewModel.resetStatus()
                showSuccessDialog = !showSuccessDialog
                viewModel.getUserWallet()
                viewModel.updatePhoneNumber("")
                viewModel.updateWithdrawalAmount("")
            },
            withdrawalAmount = formatMoneyValue(uiState.withdrawalAmount.toDouble()),
            newBalance = formatMoneyValue(uiState.userWalletData.balance),
            phoneNumber = uiState.phoneNumber.ifEmpty { uiState.userDetails.phoneNumber ?: "" }
        )
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        WithdrawalScreen(
            role = uiState.role,
            withdrawalAmount = uiState.withdrawalAmount,
            newBalance = formatMoneyValue(uiState.userWalletData.balance),
            walletBalance = formatMoneyValue(uiState.userWalletData.balance),
            phoneNumber = uiState.phoneNumber.ifEmpty { uiState.userDetails.phoneNumber ?: "" },
            onChangePhoneNumber = {
                viewModel.updatePhoneNumber(it)
                viewModel.enableButton()
            },
            onChangeWithdrawalAmount = { value ->
                val validAmount = value.filter { it.isDigit() }
                viewModel.updateWithdrawalAmount(validAmount)
                viewModel.enableButton()
            },
            withdrawalStatus = uiState.withdrawalStatus,
            buttonEnabled = uiState.buttonEnabled,
            navigateToPreviousScreen = {
                navigateToDashboardScreen()
            },
            onWithdraw = {
                if(uiState.withdrawalAmount.toDouble() > uiState.userWalletData.balance) {
                    Toast.makeText(context, "Insufficient balance in your wallet", Toast.LENGTH_LONG).show()
                } else {
                    showConfirmDialog = !showConfirmDialog
                }

            }
        )
    }
    
}

@Composable
fun WithdrawalScreen(
    role: Role,
    withdrawalAmount: String,
    newBalance: String,
    walletBalance: String,
    phoneNumber: String,
    onChangePhoneNumber: (phone: String) -> Unit,
    onChangeWithdrawalAmount: (amount: String) -> Unit,
    withdrawalStatus: WithdrawalStatus,
    buttonEnabled: Boolean,
    navigateToPreviousScreen: () -> Unit,
    onWithdraw: () -> Unit,
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
                enabled = withdrawalStatus != WithdrawalStatus.LOADING,
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
                text = "Withdraw money",
                fontSize = screenFontSize(x = 18.0).sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = "Amount",
            fontSize = screenFontSize(x = 14.0).sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        TextFieldComposable(
            shape = RoundedCornerShape(screenWidth(x = 10.0)),
            label = "Enter withdrawal amount",
            value = withdrawalAmount,
            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.NumberPassword
            ),
            onValueChange = onChangeWithdrawalAmount,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 32.0)))
        Text(
            text = walletBalance,
            fontSize = screenFontSize(x = 24.0).sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 4.0)))
        Text(
            text = "Available balance".uppercase(),
            fontWeight = FontWeight.W300,
            fontSize = screenFontSize(x = 14.0).sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 32.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.onBackground,
                painter = painterResource(id = R.drawable.withdrawal),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
            Text(
                text = "Withdraw Money To",
                fontSize = screenFontSize(x = 14.0).sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(screenWidth(x = 16.0))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mpesa_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = "M-PESA",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                TextFieldComposable(
                    shape = RoundedCornerShape(screenWidth(x = 10.0)),
                    label = "Mpesa phone number",
                    value = phoneNumber,
                    leadingIcon = R.drawable.phone,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = onChangePhoneNumber,
                    modifier = Modifier
                        .fillMaxWidth()
                )

            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            enabled = buttonEnabled && withdrawalStatus != WithdrawalStatus.LOADING,
            onClick = onWithdraw,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if(withdrawalStatus == WithdrawalStatus.LOADING) {
                Text(
                    text = "Loading...",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            } else {
                Text(
                    text = "Withdraw",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    }
}

@Composable
fun WithdrawalConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    withdrawalAmount: String,
    phoneNumber: String,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Confirm withdrawal",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Are you sure you want to withdraw $withdrawalAmount from Wazipay to M-PESA: $phoneNumber?",
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
fun WithdrawalSuccessDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    withdrawalAmount: String,
    newBalance: String,
    phoneNumber: String,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Withdrawal successful",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "You have successfully withdrawn $withdrawalAmount from Wazipay to M-PESA: $phoneNumber.",
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
                    text = "Withdraw again",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WithdrawalScreenPreview() {
    WazipayTheme {
        WithdrawalScreen(
            role = Role.BUYER,
            withdrawalAmount = "Ksh100",
            newBalance = "Ksh3450",
            walletBalance = "Ksh3500",
            phoneNumber = "",
            onChangePhoneNumber = {},
            onChangeWithdrawalAmount = {},
            withdrawalStatus = WithdrawalStatus.INITIAL,
            buttonEnabled = false,
            navigateToPreviousScreen = { /*TODO*/ },
            onWithdraw = { /*TODO*/ }
        )
    }

}