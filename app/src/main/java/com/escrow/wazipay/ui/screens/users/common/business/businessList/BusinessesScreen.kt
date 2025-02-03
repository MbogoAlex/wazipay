package com.escrow.wazipay.ui.screens.users.common.business.businessList

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.business.businesses
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.screens.users.common.business.BusinessCellComposable
import com.escrow.wazipay.ui.screens.users.common.business.LoadBusinessStatus
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

object BusinessesScreenDestination: AppNavigation {
    override val title: String = "Businesses screen"
    override val route: String = "businesses-screen"
    val ownerId: String = "ownerId"
    val routeWithOwnerId: String = "$route/{$ownerId}"
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BusinessesScreenComposable(
    homeScreen: Boolean = false,
    showBackArrow: Boolean = true,
    navigateToBusinessDetailsScreen: (businessId: String) -> Unit,
    navigateToBusinessAdditionScreen: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: BusinessViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        when(lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> {
                viewModel.getBusinessesScreenData()
            }
        }
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.loadBusinessStatus == LoadBusinessStatus.LOADING,
        onRefresh = {
            viewModel.getBusinessesScreenData()

        }
    )

    Box(
        modifier = modifier
            .safeDrawingPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        BusinessesScreen(
            pullRefreshState = pullRefreshState,
            homeScreen = homeScreen,
            showBackArrow = showBackArrow,
            role = uiState.userRole.role,
            searchQuery = uiState.searchQuery ?: "",
            onChangeSearchQuery = viewModel::updateSearchQuery,
            onClearSearchQuery = {
                viewModel.updateSearchQuery(null)
            },
            userId = uiState.userDetails.userId,
            businesses = uiState.businesses,
            navigateToBusinessDetailsScreen = navigateToBusinessDetailsScreen,
            navigateToBusinessAdditionScreen = navigateToBusinessAdditionScreen,
            navigateToPreviousScreen = navigateToPreviousScreen,
            loadBusinessStatus = uiState.loadBusinessStatus
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BusinessesScreen(
    pullRefreshState: PullRefreshState?,
    homeScreen: Boolean,
    showBackArrow: Boolean,
    role: Role,
    searchQuery: String,
    onChangeSearchQuery: (query: String) -> Unit,
    onClearSearchQuery: () -> Unit,
    userId: Int,
    businesses: List<BusinessData>,
    navigateToBusinessDetailsScreen: (businessId: String) -> Unit,
    navigateToBusinessAdditionScreen: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    loadBusinessStatus: LoadBusinessStatus,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            if(role == Role.MERCHANT) {
                FloatingActionButton(onClick = navigateToBusinessAdditionScreen) {
                    Icon(
                        painter = painterResource(id = R.drawable.add),
                        contentDescription = "Add new business"
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = screenWidth(x = 16.0),
                        end = screenWidth(x = 16.0),
                        bottom = screenHeight(x = 16.0)
                    )
            ) {
//        Text(
//            text = if(role == Role.BUYER) "Businesses" else if(role == Role.MERCHANT) "My Businesses" else "Businesses",
//            fontWeight = FontWeight.Bold,
//            fontSize = screenFontSize(x = 16.0).sp
//        )
//        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                if(showBackArrow) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = navigateToPreviousScreen) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Previous screen"
                            )
                        }
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        Text(
                            text = "My businesses",
                            fontWeight = FontWeight.Bold,
                            fontSize = screenFontSize(x = 16.0).sp
                        )
                    }
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                }
                TextField(
                    shape = RoundedCornerShape(screenWidth(x = 10.0)),
                    label = {
                        Text(
                            text = "Search business / owner",
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
                    value = searchQuery,
                    trailingIcon = {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.inverseOnSurface)
                                .padding(screenWidth(x = 5.0))
                                .clickable {
                                    onClearSearchQuery()
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
                    onValueChange = onChangeSearchQuery,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
//                if(role != Role.MERCHANT) {
//
//                }

                if(loadBusinessStatus == LoadBusinessStatus.LOADING) {
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
                    if(businesses.isNotEmpty()) {
                        LazyColumn {
                            items(businesses) { business ->
                                BusinessCellComposable(
                                    homeScreen = homeScreen,
                                    userId = userId,
                                    businessData = business,
                                    navigateToBusinessDetailsScreen = navigateToBusinessDetailsScreen
                                )
                                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                            }
                        }
                    } else {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(
                                text = "You have added no business yet",
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BusinessScreenPreview() {
    WazipayTheme {
        BusinessesScreen(
            pullRefreshState = null,
            homeScreen = false,
            showBackArrow = true,
            role = Role.BUYER,
            searchQuery = "",
            onClearSearchQuery = {},
            onChangeSearchQuery = {},
            userId = 1,
            businesses = businesses,
            navigateToBusinessDetailsScreen = {},
            navigateToBusinessAdditionScreen = {},
            navigateToPreviousScreen = {},
            loadBusinessStatus = LoadBusinessStatus.INITIAL
        )
    }
}