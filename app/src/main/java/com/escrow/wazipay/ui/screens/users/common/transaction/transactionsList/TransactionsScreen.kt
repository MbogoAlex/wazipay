package com.escrow.wazipay.ui.screens.users.common.transaction.transactionsList

import android.app.DatePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.transaction.TransactionData
import com.escrow.wazipay.data.network.models.transaction.transactions
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.screens.users.common.order.LoadOrdersStatus
import com.escrow.wazipay.ui.screens.users.common.transaction.LoadTransactionsStatus
import com.escrow.wazipay.ui.screens.users.common.transaction.TransactionCellComposable
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TransactionsScreenDestination: AppNavigation {
    override val title: String = "Transactions screen"
    override val route: String = "transactions-screen"
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionsScreenComposable(
    filtering: Boolean,
    onFilter: () -> Unit = {},
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {

    val transactionsViewModel: TransactionsViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val transactionsUiState by transactionsViewModel.uiState.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = transactionsUiState.loadTransactionsStatus == LoadTransactionsStatus.LOADING,
        onRefresh = {
            transactionsViewModel.loadTransactionsScreenUiData()

        }
    )

    Box(
        modifier = modifier
            .safeDrawingPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TransactionsScreen(
            pullRefreshState = pullRefreshState,
            userId = transactionsUiState.userDetails.userId,
            filtering = filtering,
            searchText = transactionsUiState.searchText,
            startDate = LocalDate.parse(transactionsUiState.startDate),
            endDate = LocalDate.parse(transactionsUiState.endDate),
            onChangeSearchText = transactionsViewModel::changeSearchText,
            onChangeStartDate = transactionsViewModel::changeStartDate,
            onChangeEndDate = transactionsViewModel::changeEndDate,
            onClearSearch = {
                transactionsViewModel.changeSearchText("")
            },
            transactions = transactionsUiState.transactions,
            onFilter = onFilter,
            navigateToPreviousScreen = navigateToPreviousScreen,
            loadTransactionsStatus = transactionsUiState.loadTransactionsStatus
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionsScreen(
    pullRefreshState: PullRefreshState?,
    userId: Int,
    filtering: Boolean,
    searchText: String,
    startDate: LocalDate,
    endDate: LocalDate,
    onChangeSearchText: (text: String) -> Unit,
    onChangeStartDate: (date: LocalDate) -> Unit,
    onChangeEndDate: (date: LocalDate) -> Unit,
    transactions: List<TransactionData>,
    onFilter: () -> Unit,
    onClearSearch: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    loadTransactionsStatus: LoadTransactionsStatus,
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM, yyyy")
    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        if(filtering) {
            ElevatedCard(
                shape = RoundedCornerShape(0),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = screenWidth(x = 16.0),
                            vertical = screenHeight(x = 16.0)
                        )
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        )
                        .fillMaxWidth()
                ) {
                    TextField(
                        shape = RoundedCornerShape(screenWidth(x = 10.0)),
                        leadingIcon = {
                            IconButton(onClick = onFilter) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                        label = {
                            Text(
                                text = "Search",
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        value = searchText,
                        trailingIcon = {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.inverseOnSurface)
                                    .padding(screenWidth(x = 5.0))
                                    .clickable {
                                        onClearSearch()
                                    }

                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear search",
                                    modifier = Modifier
                                        .size(screenWidth(x = 16.0))
                                )
                            }

                        },
                        onValueChange = onChangeSearchText,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                    DateRangePickerDialog(
                        startDate = startDate,
                        endDate = endDate,
                        defaultStartDate = null,
                        defaultEndDate = null,
                        onChangeStartDate = onChangeStartDate,
                        onChangeLastDate = onChangeEndDate,
                        onDismiss = { /*TODO*/ },
                        onConfirm = { /*TODO*/ }
                    )
                    TextButton(
                        onClick = onFilter,
                        modifier = Modifier
                            .align(Alignment.End)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Dismiss filtering"
                            )
                            Text(
                                text = "Dismiss",
                            )
                        }
                    }
                }
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = screenWidth(x = 16.0))
            ) {
                IconButton(onClick = navigateToPreviousScreen) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous screen"
                    )
                }
                Text(
                    text = "Transactions",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = screenFontSize(x = 16.0).sp
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = screenWidth(x = 16.0),
                        top = screenHeight(x = 16.0),
                        end = screenWidth(x = 16.0),
                        bottom = 0.dp
                    )
            ) {
                Text(
                    text = "${dateFormatter.format(startDate)} to ${dateFormatter.format(endDate)}",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onFilter) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onBackground,
                        painter = painterResource(id = R.drawable.filter),
                        contentDescription = "Filter"
                    )
                }
            }
        }

        if(loadTransactionsStatus == LoadTransactionsStatus.LOADING) {
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
            LazyColumn(
                modifier = Modifier
                    .padding(
                        horizontal = screenWidth(x = 16.0),

                        )
            ) {
                items(transactions) { item ->
                    TransactionCellComposable(
                        userId = userId,
                        role = null,
                        transactionData = item,
                        modifier = Modifier
                            .padding(
                                top = screenHeight(x = 8.0)
                            )
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateRangePickerDialog(
    startDate: LocalDate,
    endDate: LocalDate,
    defaultStartDate: String?,
    defaultEndDate: String?,
    onChangeStartDate: (date: LocalDate) -> Unit,
    onChangeLastDate: (date: LocalDate) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if(defaultStartDate != null) {
            Text(
                text = "Select date range (within $defaultStartDate and $defaultEndDate)",
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 18.0).sp,
                modifier = Modifier
                    .padding(
                        start = screenWidth(x = 16.0)
                    )
            )
        } else {
            Text(
                text = "Select date range",
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 14.0).sp,
                modifier = Modifier
                    .padding(
                        start = screenWidth(x = 16.0)
                    )
            )
        }

        DateRangePicker(
            startDate = startDate,
            endDate = endDate,
            defaultStartDate = defaultStartDate,
            defaultEndDate = defaultEndDate,
            onChangeStartDate = onChangeStartDate,
            onChangeLastDate = onChangeLastDate,
            modifier = Modifier
                .fillMaxWidth()
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateRangePicker(
    startDate: LocalDate,
    endDate: LocalDate,
    defaultStartDate: String?,
    defaultEndDate: String?,
    onChangeStartDate: (date: LocalDate) -> Unit,
    onChangeLastDate: (date: LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM, yyyy")
    val context = LocalContext.current

    // Parse the default start and end dates
    val defaultStartLocalDate = defaultStartDate?.let { LocalDate.parse(it) }
    val defaultEndLocalDate = defaultEndDate?.let { LocalDate.parse(it) }

    // Convert LocalDate to milliseconds since epoch
    val defaultStartMillis = defaultStartLocalDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    val defaultEndMillis = defaultEndLocalDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()

    val oneMonthAgo = LocalDateTime.now().minusMonths(1)

    @RequiresApi(Build.VERSION_CODES.O)
    fun showDatePicker(isStart: Boolean) {
        val initialDate = if (isStart) startDate else endDate
        val datePicker = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                if (isStart) {
                    if (selectedDate.isBefore(endDate) || selectedDate.isEqual(endDate)) {
                        onChangeStartDate(selectedDate)
                    } else {
                        // Handle case where start date is after end date
                        Toast.makeText(context, "Start date must be before end date", Toast.LENGTH_LONG).show()
                    }
                } else {
                    if (selectedDate.isAfter(startDate) || selectedDate.isEqual(startDate)) {
                        onChangeLastDate(selectedDate)
                    } else {
                        // Handle case where end date is before start date
                        Toast.makeText(context, "End date must be after start date", Toast.LENGTH_LONG).show()
                    }
                }
            },

            initialDate.year,
            initialDate.monthValue - 1,
            initialDate.dayOfMonth
        )

        // Set minimum and maximum dates
        defaultStartMillis?.let { datePicker.datePicker.minDate = it }
        defaultEndMillis?.let { datePicker.datePicker.maxDate = it }

        datePicker.show()
    }

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton(onClick = { showDatePicker(true) }) {
            Icon(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = null,
                modifier = Modifier
                    .size(screenWidth(x = 24.0))
            )
        }
        Text(
            text = dateFormatter.format(startDate),
            fontSize = screenFontSize(x = 14.0).sp
        )
        Text(
            text = "to",
            fontSize = screenFontSize(x = 14.0).sp
        )

        Text(
            text = dateFormatter.format(endDate),
            fontSize = screenFontSize(x = 14.0).sp
        )
        IconButton(onClick = { showDatePicker(false) }) {
            Icon(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = null,
                modifier = Modifier
                    .size(screenWidth(x = 24.0))
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TransactionsScreenPreview() {
    WazipayTheme {
        TransactionsScreen(
            pullRefreshState = null,
            userId = 1,
            filtering = false,
            searchText = "",
            startDate = LocalDate.now().withDayOfMonth(1),
            endDate = LocalDate.now(),
            onChangeSearchText = {},
            onChangeStartDate = {},
            onChangeEndDate = {},
            onClearSearch = {},
            transactions = transactions,
            onFilter = { /*TODO*/ },
            navigateToPreviousScreen = {},
            loadTransactionsStatus = LoadTransactionsStatus.INITIAL
        )
    }
}