package com.escrow.wazipay.ui.screens.users.common.invoice.invoicesList

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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.invoice.InvoiceData
import com.escrow.wazipay.data.network.models.invoice.invoices
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.screens.users.common.invoice.InvoiceItemComposable
import com.escrow.wazipay.ui.screens.users.common.invoice.InvoiceStatus
import com.escrow.wazipay.ui.screens.users.common.invoice.LoadInvoicesStatus
import com.escrow.wazipay.ui.screens.users.common.order.LoadOrdersStatus
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth
import kotlinx.coroutines.delay

object InvoicesScreenDestination: AppNavigation {
    override val title: String = "Invoices screen"
    override val route: String = "invoices-screen"
    val status: String = "status"
    val routeWithStatus: String = "$route/{$status}"
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InvoicesScreenComposable(
    navigateToBusinessSelectionScreenWithArgs: (toBuyerSelectionScreen: Boolean) -> Unit,
    navigateToInvoiceDetailsScreen: (invoiceId: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {

    val invoicesViewModel: InvoicesViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val invoicesUiState by invoicesViewModel.uiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        when(lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> {
                invoicesViewModel.getInvoicesScreenStartupData()
            }
        }
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = invoicesUiState.loadInvoicesStatus == LoadInvoicesStatus.LOADING,
        onRefresh = {
            invoicesViewModel.getInvoicesScreenStartupData()

        }
    )

    Box(
        modifier = modifier
            .safeDrawingPadding()
    ) {
        InvoicesScreen(
            pullRefreshState = pullRefreshState,
            role = invoicesUiState.userRole.role,
            invoices = invoicesUiState.invoices,
            statuses = invoicesUiState.statuses,
            selectedStatus = invoicesUiState.selectedStatus,
            onChangeStatus = {
                invoicesViewModel.onChangeStatus(it)

            },
            navigateToBusinessSelectionScreen = {
                navigateToBusinessSelectionScreenWithArgs(true)
            },
            navigateToInvoiceDetailsScreen = navigateToInvoiceDetailsScreen,
            navigateToPreviousScreen = navigateToPreviousScreen,
            loadInvoicesStatus = invoicesUiState.loadInvoicesStatus
        )
    }

}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InvoicesScreen(
    pullRefreshState: PullRefreshState?,
    role: Role,
    invoices: List<InvoiceData>,
    statuses: List<String>,
    selectedStatus: String,
    onChangeStatus: (status: String) -> Unit,
    navigateToBusinessSelectionScreen: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    navigateToInvoiceDetailsScreen: (invoiceId: String) -> Unit,
    loadInvoicesStatus: LoadInvoicesStatus,
    modifier: Modifier = Modifier
) {

    Scaffold(
        floatingActionButton = {
            if(role == Role.MERCHANT) {
                FloatingActionButton(onClick = navigateToBusinessSelectionScreen) {
                    Icon(
                        painter = painterResource(id = R.drawable.issue_invoice),
                        contentDescription = "Issue invoice"
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
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
                    IconButton(onClick = navigateToPreviousScreen) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous screen"
                        )
                    }
                    Text(
                        text = "${if(role == Role.BUYER) "Payments (invoices)" else "Issued Invoices"} / $selectedStatus",
                        fontWeight = FontWeight.Bold,
                        fontSize = screenFontSize(x = 14.0).sp,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
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
                if(loadInvoicesStatus == LoadInvoicesStatus.LOADING) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        PullRefreshIndicator(
                            refreshing = true,
                            state = pullRefreshState!!
                        )
                    }
                } else {
                    LazyColumn {
                        items(invoices) { invoice ->
                            InvoiceItemComposable(
                                navigateToInvoiceDetailsScreen = navigateToInvoiceDetailsScreen,
                                invoiceData = invoice
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
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
            pullRefreshState = null,
            role = Role.BUYER,
            statuses = statuses,
            selectedStatus = selectedStatus,
            onChangeStatus = {
                selectedStatus = it
            },
            invoices = invoices,
            navigateToBusinessSelectionScreen = {},
            navigateToInvoiceDetailsScreen = {},
            navigateToPreviousScreen = {},
            loadInvoicesStatus = LoadInvoicesStatus.INITIAL
        )
    }
}