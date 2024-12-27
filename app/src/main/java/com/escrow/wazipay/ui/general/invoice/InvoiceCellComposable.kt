package com.escrow.wazipay.ui.general.invoice

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.invoice.InvoiceData
import com.escrow.wazipay.utils.formatIsoDateTime
import com.escrow.wazipay.utils.formatMoneyValue
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InvoiceItemComposable(
    invoiceData: InvoiceData,
    modifier: Modifier = Modifier
) {

    val invoiceStatus = when(invoiceData.invoiceStatus) {
        "PENDING" -> "Pending"
        "ACCEPTED" -> "Paid"
        "REJECTED" -> "Rejected"
        "CANCELLED" -> "Cancelled"
        else -> "N/A"
    }.uppercase()

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = screenHeight(x = 8.0)
            )
    ) {
        Column(
            modifier = Modifier
                .padding(
                    screenWidth(x = 16.0)
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.invoice),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if(invoiceData.title.length > 25) invoiceData.title.take(25) + "..." else invoiceData.title,
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = formatIsoDateTime(LocalDateTime.parse(invoiceData.createdAt)),
                        fontSize = screenFontSize(x = 12.0).sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Column {
                    Text(
                        text = formatMoneyValue(invoiceData.amount),
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = invoiceStatus,
                        color = if(invoiceStatus.lowercase() == "paid") Color.Green else if(invoiceStatus.lowercase() == "Rejected" || invoiceStatus.lowercase() == "Cancelled") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        fontSize = screenFontSize(x = 12.0).sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            OutlinedButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "See invoice")
            }
        }
    }
}