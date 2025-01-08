package com.escrow.wazipay.ui.screens.users.specific.merchant.invoiceCreation

import android.os.Build
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.business.businessData
import com.escrow.wazipay.data.network.models.user.UserDetailsData
import com.escrow.wazipay.data.network.models.user.userDetailsData
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment.InvoiceCreationStatus
import com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment.InvoiceCreationViewModel
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.formatMoneyValue
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

object InvoiceIssuanceScreenDestination: AppNavigation {
    override val title: String = "Invoice issuance screen"
    override val route: String = "invoice-issuance-screen"
    val businessId: String = "businessId"
    val buyerId: String = "buyerId"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InvoiceIssuanceScreenComposable(
    navigateToInvoiceDetailsScreen: (invoiceId: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val viewModel: InvoiceIssuanceViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiSate by viewModel.uiState.collectAsState()

    var showConfirmDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var showSuccessDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if(uiSate.invoiceCreationStatus == InvoiceCreationStatus.SUCCESS) {
        showSuccessDialog = true
    }

    if(showConfirmDialog) {
        InvoiceConfirmationDialog(
            onConfirm = {
                showConfirmDialog = !showConfirmDialog

                viewModel.issueInvoice()
            },
            onDismiss = {
                showConfirmDialog = !showConfirmDialog
            },
            title = uiSate.title,
            cost = formatMoneyValue(uiSate.amount.toDouble())
        )
    }

    if(showSuccessDialog) {
        InvoiceCreationSuccessDialog(
            onConfirm = {
                viewModel.resetStatus()

                navigateToInvoiceDetailsScreen(uiSate.invoiceId.toString())
            },
            onDismiss = {
                viewModel.resetStatus()
                navigateToInvoiceDetailsScreen(uiSate.invoiceId.toString())
            },
            title = uiSate.title,
            cost = formatMoneyValue(uiSate.amount.toDouble())
        )
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        InvoiceIssuanceScreen(
            userId = uiSate.userDetails.userId,
            businessData = uiSate.businessData,
            title = uiSate.title,
            description = uiSate.description,
            amount = uiSate.amount,
            buyer = uiSate.buyer,
            onChangeTitle = {
                viewModel.changeTitle(it)
                viewModel.enableButton()
            },
            onChangeDescription = {
                viewModel.changeDescription(it)
                viewModel.enableButton()
            },
            onChangeAmount = { value ->
                val validAmount = value.filter { it.isDigit() }
                viewModel.changeAmount(validAmount)
                viewModel.enableButton()
            },
            invoiceCreationStatus = uiSate.invoiceCreationStatus,
            navigateToPreviousScreen = navigateToPreviousScreen,
            onCreateInvoice = {
                showConfirmDialog = !showConfirmDialog
            },
            buttonEnabled = uiSate.buttonEnabled
        )
    }
}

@Composable
fun InvoiceIssuanceScreen(
    userId: Int,
    businessData: BusinessData,
    buyer: UserDetailsData,
    title: String,
    description: String,
    amount: String,
    onChangeTitle: (title: String) -> Unit,
    onChangeDescription: (description: String) -> Unit,
    onChangeAmount: (amount: String) -> Unit,
    invoiceCreationStatus: InvoiceCreationStatus,
    navigateToPreviousScreen: () -> Unit,
    onCreateInvoice: () -> Unit,
    buttonEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = screenWidth(x = 16.0),
                vertical = screenHeight(x = 16.0)
            )
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                enabled = invoiceCreationStatus != InvoiceCreationStatus.LOADING,
                onClick = navigateToPreviousScreen
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.onBackground,
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Text(
                text = "Invoice issuance",
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 16.0).sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        TextField(
            label = {
                Text(
                    text = "Title / Product Name",
                    fontSize = screenFontSize(x = 14.0).sp,
                )
            },
            value = title,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onChangeTitle,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        TextField(
            label = {
                Text(
                    text = "Description",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            },
            value = description,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = onChangeDescription,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        TextField(
            label = {
                Text(
                    text = "Amount",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            },
            value = amount,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.NumberPassword
            ),
            onValueChange = onChangeAmount,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = "Business",
            fontWeight = FontWeight.Bold,
            fontSize = screenFontSize(x = 14.0).sp
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(screenWidth(x = 16.0))
            ) {
                if(businessData.owner?.id == userId) {
                    Text(
                        text = "My Business",
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(screenWidth(x = 4.0)))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.shop),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = businessData.name,
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Text(
                    text = businessData.description,
                    fontSize = screenFontSize(x = 14.0).sp,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 4.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.person),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(text = businessData.owner?.username ?: "")
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    Icon(
                        painter = painterResource(id = R.drawable.phone),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(text = businessData.owner?.phoneNumber ?: "")
                }
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = "Buyer",
            fontSize = screenFontSize(x = 14.0).sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))

        SelectableBuyerCell(
            userDetailsData = buyer,
            showArrow = false,
            navigateToInvoiceCreationScreen = {}
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            enabled = buttonEnabled && invoiceCreationStatus != InvoiceCreationStatus.LOADING,
            onClick = onCreateInvoice,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if(invoiceCreationStatus == InvoiceCreationStatus.LOADING) {
                Text(
                    text = "Loading...",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            } else {
                Text(
                    text = "Issue invoice",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    }
}

@Composable
fun InvoiceConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String,
    cost: String,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Confirm Business Payment",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Are you sure you want to pay business for order $title with a cost of $cost?",
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
fun InvoiceCreationSuccessDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String,
    cost: String,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Payment successful",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Your invoice for $title with a cost of $cost has been made successfully",
                fontSize = screenFontSize(x = 14.0).sp
            )
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
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InvoiceIssuanceScreenPreview() {
    WazipayTheme {
        InvoiceIssuanceScreen(
            userId = 1,
            businessData = businessData,
            buyer = userDetailsData,
            title = "",
            description = "",
            amount = "",
            onChangeTitle = {},
            onChangeDescription = {},
            onChangeAmount = {},
            invoiceCreationStatus = InvoiceCreationStatus.INITIAL,
            navigateToPreviousScreen = { /*TODO*/ },
            onCreateInvoice = { /*TODO*/ },
            buttonEnabled = false
        )
    }
}