package com.escrow.wazipay.ui.dashboard

import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.escrow.wazipay.R
import com.escrow.wazipay.ui.buyer.BuyerDashboardScreenComposable
import com.escrow.wazipay.ui.buyer.NavBarItem
import com.escrow.wazipay.ui.buyer.NavItem
import com.escrow.wazipay.ui.general.OrdersScreenComposable
import com.escrow.wazipay.ui.general.TransactionsScreenComposable
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

object DashboardScreenDestination: AppNavigation {
    override val title: String = "Dashboard screen"
    override val route: String = "dashboard-screen"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreenComposable() {
    val context = LocalContext.current

    BackHandler(onBack = {
        (context as? Activity)?.finish()
    })

    var filtering by rememberSaveable {
        mutableStateOf(false)
    }
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

    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        DashboardScreen(
            filtering = filtering,
            navItems = navItems,
            selectedTab = selectedTab,
            onSelectTab = {
                selectedTab = it
            },
            onFilter = {
                filtering = !filtering
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(
    filtering: Boolean,
    navItems: List<NavItem>,
    selectedTab: NavBarItem,
    onSelectTab: (tab: NavBarItem) -> Unit,
    onFilter: () -> Unit,
    modifier: Modifier = Modifier
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
                Text(
                    text = "Buyer Dashboard",
                    fontSize = screenFontSize(x = 16.0).sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Buyer",
                        fontSize = screenFontSize(x = 16.0).sp
                    )
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Switch role"
                        )
                    }
                }
            }
        }
//        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        when(selectedTab) {
            NavBarItem.HOME -> BuyerDashboardScreenComposable(
                modifier = Modifier
                    .weight(1f)
            )
            NavBarItem.TRANSACTIONS -> TransactionsScreenComposable(
                onFilter = onFilter,
                filtering = filtering,
                modifier = Modifier
                    .weight(1f)
            )
            NavBarItem.ORDERS -> OrdersScreenComposable(
                modifier = Modifier
                    .weight(1f)
            )
            NavBarItem.PROFILE -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    Text(text = "Profile")
                }
            }
        }
        if(!filtering) {
            BottomNavBar(
                navItems = navItems,
                selectedTab = selectedTab,
                onSelectTab = onSelectTab
            )
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
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

        DashboardScreen(
            filtering = filtering,
            navItems = navItems,
            selectedTab = selectedTab,
            onSelectTab = {
                selectedTab = it
            },
            onFilter = {
                filtering = !filtering
            }
        )
    }
}