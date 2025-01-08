package com.escrow.wazipay.ui.screens.users.common.invoice.invoiceDetails

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.invoice.InvoiceData
import com.escrow.wazipay.data.network.models.invoice.invoiceData
import com.escrow.wazipay.data.network.models.order.OrderData
import com.escrow.wazipay.data.network.models.order.orderData
import com.escrow.wazipay.data.network.models.user.UserContactData
import com.escrow.wazipay.data.network.models.user.userContactData
import com.escrow.wazipay.data.network.models.wallet.UserWalletData
import com.escrow.wazipay.data.network.models.wallet.userWalletData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.ui.screens.users.common.order.OrderItemComposable
import com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment.InvoiceCreationStatus
import com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment.PaymentMethod
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.composables.TextFieldComposable
import com.escrow.wazipay.utils.formatMoneyValue
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

@Composable
fun InvoiceDetailsScreenComposable(
    modifier: Modifier = Modifier
) {

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InvoiceDetailsScreen(
    invoiceData: InvoiceData,
    role: Role,
    userWalletData: UserWalletData,
    orderData: OrderData?,
    buyer: UserContactData,
    merchant: UserContactData,
    phoneNumber: String,
    onChangePhoneNumber: (phone: String) -> Unit,
    paymentMethod: PaymentMethod,
    onChangePaymentMethod: (paymentMethod: PaymentMethod) -> Unit,
    navigateToOrderDetailsScreen: (orderId: String, fromPaymentScreen: Boolean) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = screenHeight(x = 16.0),
                horizontal = screenWidth(x = 16.0)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = navigateToPreviousScreen) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Text(
                text = if(role == Role.BUYER) "Payment (invoice) details" else "Issued Invoice details",
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 14.0).sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            InvoiceDetailsCard(
                role = role,
                invoiceData = invoiceData,
                userWalletData = userWalletData,
                paymentMethod = paymentMethod,
                onChangePaymentMethod = onChangePaymentMethod,
                phoneNumber = phoneNumber,
                onChangePhoneNumber = onChangePhoneNumber
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            Text(
                text = "Buyer",
                fontSize = screenFontSize(x = 14.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            ActorCard(
                role = Role.BUYER,
                userContactData = buyer
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            Text(
                text = "Merchant",
                fontSize = screenFontSize(x = 14.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            ActorCard(
                role = Role.MERCHANT,
                userContactData = merchant
            )
            if(orderData != null) {
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Text(
                    text = "Order details",
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                OrderItemComposable(
                    homeScreen = false,
                    orderData = orderData,
                    navigateToOrderDetailsScreen = navigateToOrderDetailsScreen
                )
            }
        }
    }
}

@Composable
fun InvoiceDetailsCard(
    role: Role,
    invoiceData: InvoiceData,
    userWalletData: UserWalletData,
    paymentMethod: PaymentMethod,
    onChangePaymentMethod: (paymentMethod: PaymentMethod) -> Unit,
    phoneNumber: String,
    onChangePhoneNumber: (phone: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = screenWidth(x = 1.0),
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
            .padding(
                screenWidth(x = 16.0)
            )
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status",
                    fontWeight = FontWeight.Bold,
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Text(
                    text = invoiceData.invoiceStatus,
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Title",
                    fontWeight = FontWeight.Bold,
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Text(
                    text = invoiceData.title,
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Amount to pay",
                    fontWeight = FontWeight.Bold,
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Text(
                    text = formatMoneyValue(invoiceData.amount),
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = invoiceData.invoiceStatus,
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
            if(invoiceData.invoiceStatus == "PENDING" && role == Role.BUYER) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = screenWidth(x = 16.0))
                ) {
                    RadioButton(
                        selected = paymentMethod == PaymentMethod.WAZIPAY,
                        onClick = { onChangePaymentMethod(PaymentMethod.WAZIPAY) }
                    )
                    Text(
                        text = "Wazipay",
                        modifier = Modifier.padding(start = screenWidth(x = 8.0)),
                        fontSize = screenFontSize(x = 14.0).sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.width(screenWidth(x = 16.0)))

                    RadioButton(
                        selected = paymentMethod == PaymentMethod.MPESA,
                        onClick = { onChangePaymentMethod(PaymentMethod.MPESA) }
                    )
                    Text(
                        text = "M-PESA",
                        modifier = Modifier.padding(start = screenWidth(x = 8.0)),
                        fontSize = screenFontSize(x = 14.0).sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                if(paymentMethod == PaymentMethod.WAZIPAY) {
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
                                Text(text = formatMoneyValue(userWalletData.balance))
                            }
                            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Pay: ")
                                Text(text = formatMoneyValue(invoiceData.amount))
                            }

                        }
                    }
                } else if(paymentMethod == PaymentMethod.MPESA) {
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
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "Pay ${formatMoneyValue(invoiceData.amount)}")
                }
            }
        }
    }
}

@Composable
fun ActorCard(
    role: Role,
    userContactData: UserContactData,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = screenWidth(x = 1.0),
                color = Color.LightGray,
                shape = RoundedCornerShape(screenWidth(x = 10.0))
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(screenWidth(x = 16.0))
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(screenWidth(x = 16.0))
            ) {
                Icon(
                    painter = painterResource(id = if(role == Role.BUYER) R.drawable.buyer else R.drawable.seller),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
            Column {
                Text(text = com.escrow.wazipay.data.network.models.user.userContactData.username)
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.phone),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = com.escrow.wazipay.data.network.models.user.userContactData.phoneNumber,
                        fontSize = screenFontSize(x = 14.0).sp,
//                            fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.email),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = "mbogoalex3@gmail.com",
                        fontSize = screenFontSize(x = 14.0).sp,
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InvoiceDetailsScreenPreview(
    modifier: Modifier = Modifier
) {
    WazipayTheme {
        InvoiceDetailsScreen(
            invoiceData = invoiceData,
            role = Role.BUYER,
            paymentMethod = PaymentMethod.WAZIPAY,
            buyer = userContactData,
            merchant = userContactData,
            phoneNumber = "0794649026",
            onChangePaymentMethod = {},
            userWalletData = userWalletData,
            onChangePhoneNumber = {},
            navigateToPreviousScreen = {},
            orderData = orderData,
            navigateToOrderDetailsScreen = {orderId: String, fromPaymentScreen: Boolean ->}
        )
    }
}