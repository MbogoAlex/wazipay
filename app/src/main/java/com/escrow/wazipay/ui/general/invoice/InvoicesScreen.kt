package com.escrow.wazipay.ui.general.invoice

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.data.network.models.invoice.InvoiceData
import com.escrow.wazipay.data.network.models.invoice.invoices
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InvoicesScreenComposable(
    modifier: Modifier = Modifier
) {

    val invoicesViewModel: InvoicesViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val invoicesUiState by invoicesViewModel.uiState.collectAsState()


    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        InvoicesScreen(
            role = invoicesUiState.userRole.role,
            invoices = invoicesUiState.invoices,
            statuses = invoicesUiState.statuses,
            selectedStatus = invoicesUiState.selectedStatus,
            onChangeStatus = {
                invoicesViewModel.onChangeStatus(it)

            }
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InvoicesScreen(
    role: Role,
    invoices: List<InvoiceData>,
    statuses: List<String>,
    selectedStatus: String,
    onChangeStatus: (status: String) -> Unit,
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
        Text(
            text = "${if(role == Role.BUYER) "Received Invoices" else "Issued Invoices"} / $selectedStatus",
            fontWeight = FontWeight.Bold,
            fontSize = screenFontSize(x = 14.0).sp
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
        ) {
            statuses.forEach {
                if(selectedStatus == it) {
                    Button(onClick = { onChangeStatus(it) }) {
                        Text(text = it)
                    }
                } else {
                    OutlinedButton(onClick = { onChangeStatus(it) }) {
                        Text(text = it)
                    }
                }
                Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        LazyColumn {
            items(invoices) {
                InvoiceItemComposable(
                    invoiceData = it
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InvoicesScreenPreview() {
    WazipayTheme {
        val statuses = listOf("All", "Pending", "Accepted", "Rejected", "Cancelled")

        var selectedStatus by rememberSaveable {
            mutableStateOf("All")
        }
        InvoicesScreen(
            role = Role.BUYER,
            statuses = statuses,
            selectedStatus = selectedStatus,
            onChangeStatus = {
                selectedStatus = it
            },
            invoices = invoices
        )
    }
}