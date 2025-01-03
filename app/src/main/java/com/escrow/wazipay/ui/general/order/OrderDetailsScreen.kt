package com.escrow.wazipay.ui.general.order

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.order.OrderData
import com.escrow.wazipay.data.network.models.order.orderData
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.formatIsoDateTime
import com.escrow.wazipay.utils.formatMoneyValue
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth
import java.time.LocalDateTime

object OrderDetailsScreenDestination: AppNavigation {
    override val title: String = "Order details screen"
    override val route: String = "order-details-screen"
    val orderId: String = "orderId"
    val routeWithArgs: String = "$route/{$orderId}"
}

@Composable
fun OrderDetailsScreenComposable(
    modifier: Modifier = Modifier
) {

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderDetailsScreen(
    userId: Int,
    orderData: OrderData,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                vertical = screenHeight(x = 16.0),
                horizontal = screenWidth(x = 16.0)
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
                text = "Order details",
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 16.0).sp
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = orderData.orderStage == "PENDING_PICKUP", onClick = { /*TODO*/ })
            Text(
                text = "Pending pickup",
                fontSize = screenFontSize(x = 14.0).sp,
            )
        }
        VerticalDivider(
            modifier = Modifier
                .height(screenHeight(x = 32.0))
                .padding(
                    horizontal = screenWidth(x = 22.0)
                )
        )
//        Spacer(modifier = Modifier.height(screenHeight(x = 32.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = orderData.orderStage == "IN_TRANSIT", onClick = { /*TODO*/ })
            Text(
                text = "In transit",
                fontSize = screenFontSize(x = 14.0).sp,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            VerticalDivider(
                modifier = Modifier
                    .height(screenHeight(x = 32.0))
                    .padding(
                        horizontal = screenWidth(x = 22.0)
                    )
            )
            if(orderData.courier != null) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.motorbike),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Text(
                            text = "Courier:",
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Text(text = orderData.courier.username)
                    }
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
                            text = orderData.courier.phoneNumber,
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
                            text = orderData.courier.email,
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                    }
                }
            }
        }
//        Spacer(modifier = Modifier.height(screenHeight(x = 32.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = orderData.orderStage == "COMPLETE", onClick = { /*TODO*/ })
            Text(
                text = "Complete",
                fontSize = screenFontSize(x = 14.0).sp,
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 32.0)))
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
                        text = orderData.business.name,
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
                        text = "Cost: ",
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
                if(orderData.business.owner.id == userId) {
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
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrderDetailsScreenPreview() {
    WazipayTheme {
        OrderDetailsScreen(
            userId = 1,
            orderData = orderData,
            navigateToPreviousScreen = {}
        )
    }
}