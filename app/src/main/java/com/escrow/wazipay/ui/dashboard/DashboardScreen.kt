package com.escrow.wazipay.ui.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.escrow.wazipay.R
import com.escrow.wazipay.ui.buyer.BuyerDashboardScreenComposable
import com.escrow.wazipay.ui.buyer.NavBarItem
import com.escrow.wazipay.ui.buyer.NavItem
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenWidth

@Composable
fun DashboardScreenComposable() {
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
        navItems = navItems,
        selectedTab = selectedTab,
        onSelectTab = {
            selectedTab = it
        }
    )
}

@Composable
fun DashboardScreen(
    navItems: List<NavItem>,
    selectedTab: NavBarItem,
    onSelectTab: (tab: NavBarItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when(selectedTab) {
            NavBarItem.HOME -> BuyerDashboardScreenComposable(
                modifier = Modifier
                    .weight(1f)
            )
            NavBarItem.TRANSACTIONS -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    Text(text = "Transactions")
                }
            }
            NavBarItem.ORDERS -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    Text(text = "Orders")
                }
            }
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
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
            navItems = navItems,
            selectedTab = selectedTab,
            onSelectTab = {
                selectedTab = it
            }
        )
    }
}