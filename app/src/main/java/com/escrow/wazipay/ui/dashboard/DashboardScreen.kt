package com.escrow.wazipay.ui.dashboard

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import com.escrow.wazipay.R
import com.escrow.wazipay.ui.buyer.BuyerDashboardScreenComposable
import com.escrow.wazipay.ui.general.NavBarItem
import com.escrow.wazipay.ui.general.NavItem
import com.escrow.wazipay.ui.general.OrdersScreenComposable
import com.escrow.wazipay.ui.general.TransactionsScreenComposable
import com.escrow.wazipay.ui.merchant.MerchantDashboardScreenComposable
import com.escrow.wazipay.ui.nav.AppNavigation
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
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreenComposable(
    darkMode: Boolean,
    onSwitchTheme: () -> Unit,
    navigateToLoginScreenWithArgs: (phoneNumber: String, pin: String) -> Unit,
) {
    val context = LocalContext.current

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

    var selectedProfile by rememberSaveable {
        mutableStateOf("Buyer")
    }


    val navItems = when(selectedProfile) {
        "Buyer" -> listOf(
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

        "Merchant" -> listOf(
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
                name = "Shops",
                icon = R.drawable.shop,
                tab = NavBarItem.SHOPS
            ),
            NavItem(
                name = "Profile",
                icon = R.drawable.profile,
                tab = NavBarItem.PROFILE
            ),
        )

        else -> listOf()
    }

    var selectedTab by rememberSaveable {
        mutableStateOf(NavBarItem.HOME)
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
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
            selectedProfile = selectedProfile,
            dropdownExpanded = dropdownExpanded,
            onSelectProfile = {
                selectedProfile = it
                selectedTab = NavBarItem.HOME
            },
            filtering = filtering,
            navItems = navItems,
            selectedTab = selectedTab,
            onSelectTab = {
                selectedTab = it
            },
            onExpandDropdown = {
                dropdownExpanded = !dropdownExpanded
            },
            onFilter = {
                filtering = !filtering
            },
            navigateToLoginScreenWithArgs = navigateToLoginScreenWithArgs
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
    selectedProfile: String,
    onSelectProfile: (profile: String) -> Unit,
    filtering: Boolean,
    navItems: List<NavItem>,
    selectedTab: NavBarItem,
    onSelectTab: (tab: NavBarItem) -> Unit,
    onFilter: () -> Unit,
    navigateToLoginScreenWithArgs: (phoneNumber: String, pin: String) -> Unit,
    modifier: Modifier = Modifier
) {

    ModalNavigationDrawer(
        drawerState = drawerState!!,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .padding(screenWidth(x = 10.0))
                ) {
                    Spacer(modifier = Modifier.height(screenHeight(x = 10.0)))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(
                                horizontal = screenWidth(x = 16.0)
                            )
                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.ligiopen_icon),
//                            contentDescription = null,
//                        )
//                        Spacer(modifier = Modifier.width(screenWidth(x = 3.0)))
                        Text(
                            text = selectedProfile.uppercase(),
                            fontSize = screenFontSize(x = 16.0).sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        ThemeSwitcher(
                            darkTheme = darkMode,
                            size = screenWidth(x = 30.0),
                            padding = screenWidth(x = 5.0),
                            onClick = onSwitchTheme,
                            modifier = Modifier
                                .padding(
                                    end = screenWidth(x = 8.0)
                                )
                        )
                    }
                    Spacer(modifier = Modifier.height(screenHeight(x = 15.0)))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(screenHeight(x = 15.0)))
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        for(tab in navItems) {
                            NavigationDrawerItem(
                                label = {
                                    Row {
                                        Icon(
                                            painter = painterResource(id = tab.icon),
                                            contentDescription = tab.name,
                                            modifier = Modifier
                                                .size(screenWidth(x = 24.0))
                                        )
                                        Spacer(modifier = Modifier.width(screenWidth(x = 5.0)))
                                        Text(
                                            text = tab.name,
                                            fontSize = screenFontSize(x = 14.0).sp,
                                        )
                                    }
                                },
                                selected = selectedTab == tab.tab,
                                onClick = {
                                    onSelectTab(tab.tab)
                                    scope!!.launch {
                                        drawerState.close()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ElevatedCard(
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(
                            top = screenHeight(x = 8.0),
                            start = screenWidth(x = 16.0),
                            end = screenWidth(x = 16.0),
                            bottom = screenHeight(x = 8.0)
                        )
                ) {
                    IconButton(onClick = {
                        scope!!.launch {
                            if(drawerState.isClosed) drawerState.open() else drawerState.close()
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.menu),
                            contentDescription = "Menu",
                            modifier = Modifier
                                .size(screenWidth(x = 24.0))
                        )
                    }
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = "WAZIPAY",
                        fontSize = screenFontSize(x = 16.0).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    onExpandDropdown()
                                }
                        ) {
                            Text(
                                text = selectedProfile,
                                fontSize = screenFontSize(x = 16.0).sp
                            )
                            IconButton(onClick = onExpandDropdown) {
                                Icon(
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
                                        if(it == selectedProfile) {
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
                                                    fontSize = screenFontSize(x = 14.0).sp
                                                )
                                            }
                                        } else {
                                            Text(
                                                text = it,
                                                fontSize = screenFontSize(x = 14.0).sp
                                            )
                                        }
                                    },
                                    onClick = {
                                        onSelectProfile(it)
                                        onExpandDropdown()
                                    }
                                )
                            }
                        }
                    }
                }
            }
//        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            when(selectedTab) {
                NavBarItem.HOME -> when(selectedProfile) {
                    "Buyer" -> BuyerDashboardScreenComposable(
                        navigateToLoginScreenWithArgs = navigateToLoginScreenWithArgs,
                        modifier = Modifier
//                            .weight(1f)
                    )
                    "Merchant" -> MerchantDashboardScreenComposable(
                        modifier = Modifier
//                            .weight(1f)
                    )

                }
                NavBarItem.TRANSACTIONS -> TransactionsScreenComposable(
                    onFilter = onFilter,
                    filtering = filtering,
                    modifier = Modifier
//                        .weight(1f)
                )
                NavBarItem.ORDERS -> OrdersScreenComposable(
                    profile = selectedProfile,
                    modifier = Modifier
//                        .weight(1f)
                )
                NavBarItem.SHOPS -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
//                            .weight(1f)
                    ) {
                        Text(text = "Shops")
                    }
                }
                NavBarItem.PROFILE -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
//                            .weight(1f)
                    ) {
                        Text(text = "Profile")
                    }
                }
            }
        }
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
            onSelectProfile = {},
            profiles = profiles,
            selectedProfile = "Buyer",
            onFilter = {
                filtering = !filtering
            },
            navigateToLoginScreenWithArgs = {phoneNumber, pin ->  }
        )
    }
}