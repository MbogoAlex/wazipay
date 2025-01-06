package com.escrow.wazipay.ui.merchant.courier

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.business.businessData
import com.escrow.wazipay.data.network.models.business.businesses
import com.escrow.wazipay.data.network.models.order.OrderData
import com.escrow.wazipay.data.network.models.order.orderData
import com.escrow.wazipay.data.network.models.order.orders
import com.escrow.wazipay.data.network.models.user.UserDetailsData
import com.escrow.wazipay.data.network.models.user.userDetailsData
import com.escrow.wazipay.data.network.models.user.users
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.ui.buyer.invoice.InvoiceCreationStatus
import com.escrow.wazipay.ui.buyer.invoice.PaymentMethod
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.composables.TextFieldComposable
import com.escrow.wazipay.utils.formatIsoDateTime
import com.escrow.wazipay.utils.formatMoneyValue
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth
import java.time.LocalDateTime

object CourierAssignmentScreenDestination: AppNavigation {
    override val title: String = "Courier assignment screen"
    override val route: String = "courier-assignment-screen"
    val orderId: String = "orderId"
    val courierId: String = "courierId"
    val routeWithArgs: String = "$route/{$orderId}/{$courierId}"
}

@Composable
fun CourierAssignmentScreenComposable(
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CourierAssignmentScreen(
    userId: Int,
    role: Role,
    amount: String,
    onChangeAmount: (amount: String) -> Unit,
    userDetailData: UserDetailsData,
    orderData: OrderData,
    paymentMethod: PaymentMethod,
    currentBalance: String,
    phoneNumber: String,
    onChangePhoneNumber: (number: String) -> Unit,
    onChangePaymentMethod: (paymentMethod: PaymentMethod) -> Unit,
    navigateToPreviousScreen: () -> Unit,
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
//                enabled = invoiceCreationStatus != InvoiceCreationStatus.LOADING,
                onClick = navigateToPreviousScreen
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Text(
                text = "Courier Assignment",
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 16.0).sp
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Enter the delivery cost",
                fontSize = screenFontSize(x = 14.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            TextField(
                label = {
                    Text(
                        text = "Delivery cost",
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
                    selected = paymentMethod == PaymentMethod.WAZIPAY,
                    onClick = { onChangePaymentMethod(PaymentMethod.WAZIPAY) }
                )
                Text(
                    text = "Wazipay",
                    modifier = Modifier.padding(start = screenWidth(x = 8.0)),
                    fontSize = screenFontSize(x = 14.0).sp
                )

                Spacer(modifier = Modifier.width(screenWidth(x = 16.0)))

                RadioButton(
                    selected = paymentMethod == PaymentMethod.MPESA,
                    onClick = { onChangePaymentMethod(PaymentMethod.MPESA) }
                )
                Text(
                    text = "M-PESA",
                    modifier = Modifier.padding(start = screenWidth(x = 8.0)),
                    fontSize = screenFontSize(x = 14.0).sp
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
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            Text(
                text = "Courier:",
                fontSize = screenFontSize(x = 14.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            SelectableCourierCell(
                userDetailsData = userDetailData,
                showArrow = false
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = screenWidth(x = 1.0),
                        color = Color.LightGray,
                        shape = RoundedCornerShape(screenWidth(x = 10.0))
                    )
                    .padding(screenWidth(x = 16.0))
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Order code: ",
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.W300
                        )
                        Text(
                            text = orderData.orderCode,
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Business: ",
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.W300
                        )
                        Text(
                            text = orderData.business?.name ?: "",
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    Text(
                        text = orderData.name,
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = orderData.description,
                        fontSize = screenFontSize(x = 14.0).sp,
                    )
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    Text(
                        text = formatIsoDateTime(LocalDateTime.parse(orderData.createdAt)),
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if(role == Role.MERCHANT) "Paid amount: " else "Cost: ",
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.W300
                        )
                        Text(
                            text = formatMoneyValue(orderData.productCost),
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    if(orderData.business?.owner?.id == userId) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Delivery cost: ",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.W300
                            )
                            Text(
                                text = if(orderData.deliveryCost != null) formatMoneyValue(orderData.deliveryCost) else "N/A",
                                fontSize = screenFontSize(x = 14.0).sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if(orderData.buyer?.id != userId) {
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                        Text(
                            text = "Buyer:",
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.Bold
                        )
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
                                text = orderData.buyer?.phoneNumber ?: "",
                                fontSize = screenFontSize(x = 14.0).sp,
//                            fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Icon(
                                painter = painterResource(id = R.drawable.email),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Text(
                                text = orderData.buyer?.email ?: "",
                                fontSize = screenFontSize(x = 14.0).sp,
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Confirm assignment",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CourierAssignmentScreenPreview() {
    WazipayTheme {
        CourierAssignmentScreen(
            userId = 1,
            role = Role.BUYER,
            amount = "",
            onChangeAmount = {},
            userDetailData = users[0],
            orderData = orders[0],
            paymentMethod = PaymentMethod.WAZIPAY,
            currentBalance = formatMoneyValue(2500.0),
            phoneNumber = "",
            onChangePhoneNumber = {},
            onChangePaymentMethod = {},
            navigateToPreviousScreen = {}
        )
    }
}