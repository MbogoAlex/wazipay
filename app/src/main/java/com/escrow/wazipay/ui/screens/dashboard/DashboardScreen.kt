package com.escrow.wazipay.ui.screens.dashboard

import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.R
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.screens.users.common.NavBarItem
import com.escrow.wazipay.ui.screens.users.common.NavItem
import com.escrow.wazipay.ui.screens.users.common.business.businessList.BusinessesScreenComposable
import com.escrow.wazipay.ui.screens.users.common.invoice.invoicesList.InvoicesScreenComposable
import com.escrow.wazipay.ui.screens.users.common.order.ordersList.OrdersScreenComposable
import com.escrow.wazipay.ui.screens.users.common.profile.ProfileScreenComposable
import com.escrow.wazipay.ui.screens.users.common.transaction.transactionsList.TransactionsScreenComposable
import com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment.BusinessSelectionScreenComposable
import com.escrow.wazipay.ui.screens.users.specific.buyer.dashboard.BuyerDashboardScreenComposable
import com.escrow.wazipay.ui.screens.users.specific.merchant.dashboard.MerchantDashboardScreenComposable
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object DashboardScreenDestination: AppNavigation {
    override val title: String = "Dashboard screen"
    override val route: String = "dashboard-screen"
    val child: String = "child"
    val routeWithChild: String = "$route/{$child}"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreenComposable(
    darkMode: Boolean,
    onSwitchTheme: () -> Unit,
    navigateToLoginScreenWithArgs: (phoneNumber: String, pin: String) -> Unit,
    navigateToBusinessDetailsScreen: (businessId: String) -> Unit,
    navigateToDepositScreen: () -> Unit,
    navigateToWithdrawalScreen: () -> Unit,
    navigateToDashboardScreen: () -> Unit,
    navigateToBusinessSelectionScreen: () -> Unit,
    navigateToOrderDetailsScreen: (orderId: String, fromPaymentScreen: Boolean) -> Unit,
    navigateToInvoiceCreationScreen: (businessId: String) -> Unit,
    navigateToOrdersScreen: () -> Unit,
    navigateToInvoicesScreen: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    navigateToTransactionsScreen: () -> Unit,
) {
    val context = LocalContext.current

    val viewModel: DashboardViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    BackHandler(onBack = {
        (context as? Activity)?.finish()
    })

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    var filtering by rememberSaveable {
        mutableStateOf(false)
    }

    val profiles = listOf(
        "Buyer",
        "Merchant",
        "Courier"
    )

    var dropdownExpanded by rememberSaveable {
        mutableStateOf(false)
    }



    val navItems = when(uiState.userRole.role) {
        Role.BUYER -> {
            listOf(
                NavItem(
                    name = "Home",
                    icon = R.drawable.home,
                    tab = NavBarItem.HOME
                ),
                NavItem(
                    name = "Pay business",
                    icon = R.drawable.pay,
                    tab = NavBarItem.PAY_BUSINESS
                ),
//                NavItem(
//                    name = "Orders",
//                    icon = R.drawable.orders,
//                    tab = NavBarItem.ORDERS
//                ),
//                NavItem(
//                    name = "Invoices",
//                    icon = R.drawable.invoice,
//                    tab = NavBarItem.INVOICES
//                ),
//                NavItem(
//                    name = "Businesses",
//                    icon = R.drawable.shop,
//                    tab = NavBarItem.BUSINESSES
//                ),
//                NavItem(
//                    name = "Transactions",
//                    icon = R.drawable.transactions,
//                    tab = NavBarItem.TRANSACTIONS
//                ),
                NavItem(
                    name = "Profile",
                    icon = R.drawable.profile,
                    tab = NavBarItem.PROFILE
                ),
            )
        }

        Role.MERCHANT -> {
            listOf(
                NavItem(
                    name = "Home",
                    icon = R.drawable.home,
                    tab = NavBarItem.HOME
                ),
                NavItem(
                    name = "Orders",
                    icon = R.drawable.orders,
                    tab = NavBarItem.ORDERS
                ),
                NavItem(
                    name = "Invoices",
                    icon = R.drawable.invoice,
                    tab = NavBarItem.INVOICES
                ),
                NavItem(
                    name = "Businesses",
                    icon = R.drawable.shop,
                    tab = NavBarItem.BUSINESSES
                ),
                NavItem(
                    name = "Transactions",
                    icon = R.drawable.transactions,
                    tab = NavBarItem.TRANSACTIONS
                ),
                NavItem(
                    name = "Profile",
                    icon = R.drawable.profile,
                    tab = NavBarItem.PROFILE
                ),
            )
        }

        else -> listOf()
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        DashboardScreen(
            darkMode = darkMode,
            onSwitchTheme = {
                scope.launch {
                    onSwitchTheme()
                    Toast.makeText(context, "Theme switched", Toast.LENGTH_SHORT).show()
                    delay(1000)
                    drawerState.close()
                }
            },
            drawerState = drawerState,
            scope = scope,
            profiles = profiles,
            role = uiState.userRole.role,
            dropdownExpanded = dropdownExpanded,
            onSelectRole = {
                viewModel.switchRole(it)
                navigateToDashboardScreen()
            },
            filtering = filtering,
            navItems = navItems,
            selectedTab = uiState.child,
            onSelectTab = {
                viewModel.changeTab(it)
            },
            onExpandDropdown = {
                dropdownExpanded = !dropdownExpanded
            },
            onFilter = {
                filtering = !filtering
            },
            navigateToLoginScreenWithArgs = navigateToLoginScreenWithArgs,
            navigateToDepositScreen = navigateToDepositScreen,
            navigateToWithdrawalScreen = navigateToWithdrawalScreen,
            navigateToBusinessDetailsScreen = navigateToBusinessDetailsScreen,
            navigateToBusinessSelectionScreen = navigateToBusinessSelectionScreen,
            onLogout = {
                scope.launch {
                    val phoneNumber = uiState.userDetails.phoneNumber
                    val pin = uiState.userDetails.pin
                    viewModel.deleteUsers()
                    delay(2000)
                    navigateToLoginScreenWithArgs(phoneNumber ?: "", pin ?: "")
                }
            },
            navigateToOrderDetailsScreen = navigateToOrderDetailsScreen,
            navigateToInvoiceCreationScreen = navigateToInvoiceCreationScreen,
            navigateToPreviousScreen = navigateToPreviousScreen,
            navigateToOrderScreen = navigateToOrdersScreen,
            navigateToInvoicesScreen = navigateToInvoicesScreen,
            navigateToTransactionsScreen = navigateToTransactionsScreen
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(
    onSwitchTheme: () -> Unit,
    darkMode: Boolean,
    scope: CoroutineScope?,
    drawerState: DrawerState?,
    profiles: List<String>,
    dropdownExpanded: Boolean,
    onExpandDropdown: () -> Unit,
    role: Role,
    onSelectRole: (role: Role) -> Unit,
    filtering: Boolean,
    navItems: List<NavItem>,
    selectedTab: NavBarItem,
    onSelectTab: (tab: NavBarItem) -> Unit,
    onFilter: () -> Unit,
    navigateToLoginScreenWithArgs: (phoneNumber: String, pin: String) -> Unit,
    navigateToDepositScreen: () -> Unit,
    navigateToWithdrawalScreen: () -> Unit,
    navigateToBusinessDetailsScreen: (businessId: String) -> Unit,
    navigateToBusinessSelectionScreen: () -> Unit,
    onLogout: () -> Unit,
    navigateToOrderDetailsScreen: (orderId: String, fromPaymentScreen: Boolean) -> Unit,
    navigateToInvoiceCreationScreen: (businessId: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    navigateToOrderScreen: () -> Unit,
    navigateToInvoicesScreen: () -> Unit,
    navigateToTransactionsScreen: () -> Unit,
    modifier: Modifier = Modifier
) {

    val title = when(selectedTab) {
        NavBarItem.HOME -> "Dashboard"
        NavBarItem.TRANSACTIONS -> "Transactions"
        NavBarItem.ORDERS -> "Orders"
        NavBarItem.PROFILE -> "Profile"
        NavBarItem.BUSINESSES -> "Businesses"
        NavBarItem.INVOICES -> "Invoices"
        NavBarItem.SHOPS -> "Shops"
        NavBarItem.PAY_BUSINESS -> "Pay business"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(
                    start = screenWidth(x = 16.0),
                    top = screenHeight(x = 8.0),
                    end = screenWidth(x = 16.0),
                    bottom = screenHeight(x = 8.0)
                )
        ) {
            Text(
                text = title,
                fontSize = screenFontSize(x = 16.0).sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Switch role",
                    fontSize = screenFontSize(x = 14.0).sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Icon(
                    painter = painterResource(id = R.drawable.double_arrow_right),
                    contentDescription = null
                )
            }
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            onExpandDropdown()
                        }
                ) {
                    Text(
                        text = role.name.lowercase().replaceFirstChar { it.uppercase() },
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = screenFontSize(x = 16.0).sp
                    )
                    IconButton(onClick = onExpandDropdown) {
                        Icon(
                            tint = MaterialTheme.colorScheme.onBackground,
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Switch role"
                        )
                    }
                }
                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = onExpandDropdown
                ) {
                    profiles.forEach {
                        DropdownMenuItem(
                            text = {
                                if(it.lowercase() == role.name.lowercase()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.check),
                                            contentDescription = null
                                        )
                                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                                        Text(
                                            text = it,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            fontSize = screenFontSize(x = 14.0).sp
                                        )
                                    }
                                } else {
                                    Text(
                                        text = it,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = screenFontSize(x = 14.0).sp
                                    )
                                }
                            },
                            onClick = {
                                onSelectRole(Role.valueOf(it.uppercase()))
                                onExpandDropdown()
                            }
                        )
                    }
                }
            }
        }
        when(selectedTab) {
            NavBarItem.HOME -> when(role) {
                Role.BUYER -> BuyerDashboardScreenComposable(
                    navigateToLoginScreenWithArgs = navigateToLoginScreenWithArgs,
                    navigateToDepositScreen = navigateToDepositScreen,
                    navigateToWithdrawalScreen = navigateToWithdrawalScreen,
                    navigateToBusinessSelectionScreen = navigateToBusinessSelectionScreen,
                    navigateToOrderDetailsScreen = navigateToOrderDetailsScreen,
                    navigateToOrderScreen = navigateToOrderScreen,
                    navigateToInvoicesScreen = navigateToInvoicesScreen,
                    navigateToTransactionsScreen = navigateToTransactionsScreen,
                    modifier = Modifier
                            .weight(1f)
                )
                Role.MERCHANT -> MerchantDashboardScreenComposable(
                    navigateToDepositScreen = navigateToDepositScreen,
                    navigateToWithdrawalScreen = navigateToWithdrawalScreen,
                    navigateToOrderDetailsScreen = navigateToOrderDetailsScreen,
                    navigateToLoginScreenWithArgs = navigateToLoginScreenWithArgs,
                    modifier = Modifier
                            .weight(1f)
                )

                Role.COURIER -> {}
            }
            NavBarItem.TRANSACTIONS -> TransactionsScreenComposable(
                onFilter = onFilter,
                filtering = filtering,
                navigateToPreviousScreen = navigateToPreviousScreen,
                modifier = Modifier
                        .weight(1f)
            )
            NavBarItem.ORDERS -> OrdersScreenComposable(
                navigateToLoginScreenWithArgs = navigateToLoginScreenWithArgs,
                navigateToOrderDetailsScreen = navigateToOrderDetailsScreen,
                navigateToPreviousScreen = navigateToPreviousScreen,
                modifier = Modifier
                        .weight(1f)
            )
            NavBarItem.SHOPS -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
//                            .weight(1f)
                ) {
                    Text(
                        text = "Shops",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            NavBarItem.PROFILE -> ProfileScreenComposable(
                modifier = Modifier
                    .weight(1f)
            )

            NavBarItem.BUSINESSES -> BusinessesScreenComposable(
                navigateToBusinessDetailsScreen = navigateToBusinessDetailsScreen,
                modifier = Modifier
                    .weight(1f)
            )
            NavBarItem.INVOICES -> InvoicesScreenComposable(
                navigateToPreviousScreen = navigateToPreviousScreen,
                modifier = Modifier
                    .weight(1f)
            )

            NavBarItem.PAY_BUSINESS -> BusinessSelectionScreenComposable(
                navigateToInvoiceCreationScreen = navigateToInvoiceCreationScreen,
                navigateToPreviousScreen = navigateToPreviousScreen,
                showBackArrow = false,
                modifier = Modifier
                    .weight(1f)
            )
        }
        BottomNavBar(
            navItems = navItems,
            selectedTab = selectedTab,
            onSelectTab = onSelectTab
        )
    }
}

@Composable
fun BottomNavBar(
    navItems: List<NavItem>,
    selectedTab: NavBarItem,
    onSelectTab: (tab: NavBarItem) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar {
        for(item in navItems) {
            NavigationBarItem(
                label = {
                    Text(
                        text = item.name,
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                },
                selected = selectedTab == item.tab,
                onClick = {
                    onSelectTab(item.tab)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.name
                    )
                }
            )
        }
    }
}

@Composable
fun ThemeSwitcher(
    darkTheme: Boolean = false,
    size: Dp = screenWidth(x = 150.0),
    iconSize: Dp = size / 3,
    padding: Dp = screenWidth(x = 10.0),
    borderWidth: Dp = screenWidth(x = 1.0),
    parentShape: Shape = CircleShape,
    toggleShape: Shape = CircleShape,
    animationSpec: AnimationSpec<Dp> = tween(durationMillis = 300),
    onClick: () -> Unit,
    modifier: Modifier =Modifier
) {
    val offset by animateDpAsState(
        targetValue = if (darkTheme) 0.dp else size,
        animationSpec = animationSpec
    )

    Box(modifier = modifier
        .width(size * 2)
        .height(size)
        .clip(shape = parentShape)
        .clickable { onClick() }
        .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .offset(x = offset)
                .padding(all = padding)
                .clip(shape = toggleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {}
        Row(
            modifier = Modifier
                .border(
                    border = BorderStroke(
                        width = borderWidth,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    shape = parentShape
                )
        ) {
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    tint = if (darkTheme) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.primary,
                    painter = painterResource(id = R.drawable.dark_mode),
                    contentDescription = "Theme icon",
                    modifier = Modifier.size(iconSize),
                )
            }
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    tint = if (darkTheme) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondaryContainer,
                    painter = painterResource(id = R.drawable.light_mode),
                    contentDescription = "Theme icon",
                    modifier = Modifier.size(iconSize),
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    val profiles = listOf(
        "Buyer",
        "Merchant",
        "Courier"
    )
    var filtering by rememberSaveable {
        mutableStateOf(false)
    }
    WazipayTheme {
        val navItems = listOf(
            NavItem(
                name = "Home",
                icon = R.drawable.home,
                tab = NavBarItem.HOME
            ),
            NavItem(
                name = "Transactions",
                icon = R.drawable.transactions,
                tab = NavBarItem.TRANSACTIONS
            ),
            NavItem(
                name = "Orders",
                icon = R.drawable.orders,
                tab = NavBarItem.ORDERS
            ),
            NavItem(
                name = "Profile",
                icon = R.drawable.profile,
                tab = NavBarItem.PROFILE
            ),
        )
        var selectedTab by rememberSaveable {
            mutableStateOf(NavBarItem.HOME)
        }

        var darkMode by rememberSaveable {
            mutableStateOf(false)
        }

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        val scope = rememberCoroutineScope()

        DashboardScreen(
            darkMode = darkMode,
            onSwitchTheme = {
                darkMode = !darkMode
            },
            drawerState = drawerState,
            scope = scope,
            filtering = filtering,
            navItems = navItems,
            selectedTab = selectedTab,
            onSelectTab = {
                selectedTab = it
            },
            onExpandDropdown = {},
            dropdownExpanded = true,
            onSelectRole = {},
            profiles = profiles,
            role = Role.BUYER,
            onFilter = {
                filtering = !filtering
            },
            navigateToLoginScreenWithArgs = {phoneNumber, pin ->  },
            navigateToDepositScreen = {},
            navigateToWithdrawalScreen = {},
            navigateToBusinessDetailsScreen = {},
            navigateToBusinessSelectionScreen = {},
            onLogout = {},
            navigateToOrderDetailsScreen = {orderId, fromPaymentScreen ->  },
            navigateToInvoiceCreationScreen = {},
            navigateToPreviousScreen = {},
            navigateToOrderScreen = {},
            navigateToInvoicesScreen = {},
            navigateToTransactionsScreen = {}
        )
    }
}