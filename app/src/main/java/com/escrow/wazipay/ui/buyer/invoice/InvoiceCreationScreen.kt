package com.escrow.wazipay.ui.buyer.invoice

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.business.businessData
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.composables.TextFieldComposable
import com.escrow.wazipay.utils.formatMoneyValue
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

@Composable
fun InvoiceCreationScreenComposable(
    modifier: Modifier = Modifier
) {
    var paymentMethod by rememberSaveable {
        mutableStateOf("wazipay")
    }
}

@Composable
fun InvoiceCreationScreen(
    userId: Int,
    businessData: BusinessData,
    title: String,
    description: String,
    amount: String,
    phoneNumber: String,
    onChangePhoneNumber: (number: String) -> Unit,
    paymentMethod: String,
    currentBalance: String,
    onChangePaymentMethod: (method: String) -> Unit,
    onChangeTitle: (title: String) -> Unit,
    onChangeDescription: (description: String) -> Unit,
    onChangeAmount: (amount: String) -> Unit,
    invoiceCreationStatus: InvoiceCreationStatus,
    navigateToPreviousScreen: () -> Unit,
    onCreateInvoice: () -> Unit,
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
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Text(
                text = "Invoice Creation",
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 16.0).sp
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        TextField(
            label = {
                Text(
                    text = "Title / Product Name",
                    fontSize = screenFontSize(x = 14.0).sp
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
                if(businessData.owner.id == userId) {
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
                    Text(text = businessData.owner.username)
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    Icon(
                        painter = painterResource(id = R.drawable.phone),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(text = businessData.owner.phoneNumber)
                }
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = "Payment method",
            fontSize = screenFontSize(x = 14.0).sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = screenWidth(x = 16.0))
        ) {
            RadioButton(
                selected = paymentMethod == "wazipay",
                onClick = { onChangePaymentMethod("wazipay") }
            )
            Text(
                text = "Wazipay",
                modifier = Modifier.padding(start = screenWidth(x = 8.0)),
                fontSize = screenFontSize(x = 14.0).sp
            )

            Spacer(modifier = Modifier.width(screenWidth(x = 16.0)))

            RadioButton(
                selected = paymentMethod == "Mpesa",
                onClick = { onChangePaymentMethod("Mpesa") }
            )
            Text(
                text = "M-PESA",
                modifier = Modifier.padding(start = screenWidth(x = 8.0)),
                fontSize = screenFontSize(x = 14.0).sp
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        if(paymentMethod == "wazipay") {
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
                            painter = painterResource(id = R.drawable.wazipay_logo),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Text(
                            text = "Wazipay wallet",
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                    }
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Current balance: ")
                        Text(text = currentBalance)
                    }
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Pay: ")
                        Text(text = if(amount.isNotEmpty()) formatMoneyValue(amount.toDouble()) else formatMoneyValue(0.0))
                    }

                }
            }
        } else if(paymentMethod == "Mpesa") {
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
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
//            enabled = buttonEnabled && orderCreationStatus != OrderCreationStatus.LOADING,
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
                    text = "Pay business (Make order)",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InvoiceCreationScreenPreview() {
    WazipayTheme {
        InvoiceCreationScreen(
            userId = 1,
            businessData = businessData,
            title = "",
            description = "",
            amount = "",
            phoneNumber = "0794649026",
            currentBalance = formatMoneyValue(1450.0),
            onChangePhoneNumber = {},
            onChangeTitle = {},
            onChangeDescription = {},
            onChangeAmount = {},
            paymentMethod = "wazipay",
            onChangePaymentMethod = {},
            invoiceCreationStatus = InvoiceCreationStatus.INITIAL,
            navigateToPreviousScreen = { /*TODO*/ },
            onCreateInvoice = { /*TODO*/ }
        )
    }
}