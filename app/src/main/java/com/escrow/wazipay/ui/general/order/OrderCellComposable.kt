package com.escrow.wazipay.ui.general.order

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.order.OrderData
import com.escrow.wazipay.utils.formatIsoDateTime
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderItemComposable(
    homeScreen: Boolean,
    orderData: OrderData,
    modifier: Modifier = Modifier
) {
    val stage = when(orderData.orderStage) {
        "PENDING_PICKUP" -> "Pending Pickup"
        "IN_TRANSIT" -> "In Transit"
        "COMPLETE" -> "Delivered"
        "CANCELLED" -> "Cancelled"
        "REFUNDED" -> "Refunded"
        else -> "Unknown"
    }

    if(homeScreen) {
        Card(
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .padding(screenWidth(x = 16.0))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.shop),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    Text(
                        text = orderData.business.name,
                        fontSize = screenFontSize(x = 16.0).sp
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Text(
                    text = orderData.description,
                    fontSize = screenFontSize(x = 16.0).sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Code: ",
                        fontSize = screenFontSize(x = 16.0).sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = orderData.orderCode,
                        fontSize = screenFontSize(x = 16.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stage,
                        color = if(stage == "Delivered") Color.Green else if(stage == "Cancelled") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                    if(stage == "Delivered") {
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Icon(
                            painter = painterResource(id = R.drawable.check),
                            contentDescription = null
                        )
                    }
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                if(stage == "Delivered") {
                    Text(
                        text = if(orderData.completedAt != null) formatIsoDateTime(LocalDateTime.parse(orderData.completedAt)) else "Time N/A",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
                if(stage == "Cancelled") {
                    Text(
                        text = if(orderData.cancelledAt != null) formatIsoDateTime(LocalDateTime.parse(orderData.cancelledAt)) else "Time N/A",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Column(
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "Ordered at ",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                    Spacer(modifier = Modifier.height(screenHeight(x = 4.0)))
                    Text(
                        text = formatIsoDateTime(LocalDateTime.parse(orderData.createdAt)),
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }

            }
        }
    } else {
        Card(
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .padding(screenWidth(x = 16.0))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.shop),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    Text(
                        text = orderData.business.name,
                        fontSize = screenFontSize(x = 16.0).sp
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Row {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = orderData.description,
                            fontSize = screenFontSize(x = 16.0).sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .weight(1f)
                        )
                        Text(
                            text = "Code: ",
                            fontSize = screenFontSize(x = 16.0).sp
                        )
                        Text(
                            text = orderData.orderCode,
                            fontSize = screenFontSize(x = 16.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stage,
                        color = if(stage == "Delivered") Color.Green else if(stage == "Cancelled") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                    if(stage == "Delivered") {
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Icon(
                            painter = painterResource(id = R.drawable.check),
                            contentDescription = null
                        )
                    }
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                if(stage == "Delivered") {
                    Text(
                        text = if(orderData.completedAt != null) formatIsoDateTime(LocalDateTime.parse(orderData.completedAt)) else "Time N/A",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
                if(stage == "Cancelled") {
                    Text(
                        text = if(orderData.cancelledAt != null) formatIsoDateTime(LocalDateTime.parse(orderData.cancelledAt)) else "Time N/A",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ordered at ",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                    Text(
                        text = formatIsoDateTime(LocalDateTime.parse(orderData.createdAt)),
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }

            }
        }
    }


}