package com.escrow.wazipay.ui.screens.users.common.business.businessDetails

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.business.businessData
import com.escrow.wazipay.data.network.models.user.UserContactData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.screens.users.common.business.LoadBusinessStatus
import com.escrow.wazipay.ui.screens.users.common.invoice.LoadInvoicesStatus
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

object BusinessDetailsScreenDestination: AppNavigation {
    override val title: String = "Business details screen"
    override val route: String = "business-details-screen"
    val businessId: String = "businessId"
    val routeWithArgs: String = "$route/{$businessId}"
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BusinessDetailsScreenComposable(
    navigateToOrdersScreenWithArgs: (businessId: String) -> Unit,
    navigateToCreateOrderScreenWithArgs: (businessId: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: BusinessDetailsViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.loadBusinessStatus == LoadBusinessStatus.LOADING,
        onRefresh = {
            viewModel.getBusiness()

        }
    )

    Box(
        modifier = Modifier
            .safeDrawingPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        BusinessDetailsScreen(
            pullRefreshState = pullRefreshState,
            userId = uiState.userDetails.userId,
            role = uiState.role,
            businessData = uiState.businessData,
            navigateToOrdersScreenWithArgs = navigateToOrdersScreenWithArgs,
            navigateToCreateOrderScreenWithArgs = navigateToCreateOrderScreenWithArgs,
            navigateToPreviousScreen = navigateToPreviousScreen,
            loadBusinessStatus = uiState.loadBusinessStatus
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BusinessDetailsScreen(
    pullRefreshState: PullRefreshState?,
    userId: Int,
    role: Role,
    businessData: BusinessData,
    navigateToOrdersScreenWithArgs: (businessId: String) -> Unit,
    navigateToCreateOrderScreenWithArgs: (businessId: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    loadBusinessStatus: LoadBusinessStatus,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = screenHeight(x = 16.0),
                horizontal = screenWidth(x = 16.0)
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
            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
            Text(
                text = "Business details",
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 16.0).sp
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
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
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = businessData.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = screenFontSize(x = 16.0).sp
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Text(
                    text = businessData.description,
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Text(
                    text = "Products",
                    fontWeight = FontWeight.Bold,
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                businessData.products.forEach {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.circle),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 8.0))
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Text(
                            text = it,
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Text(
                    text = "About the merchant",
                    fontWeight = FontWeight.Bold,
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                ActorCard(
                    role = Role.MERCHANT,
                    userContactData = businessData.owner ?: UserContactData(0, "", "", "")
                )
            }
        }
    }
}

@Composable
fun ActorCard(
    role: Role,
    userContactData: UserContactData,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = screenWidth(x = 1.0),
                color = Color.LightGray,
                shape = RoundedCornerShape(screenWidth(x = 10.0))
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(screenWidth(x = 16.0))
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(screenWidth(x = 16.0))
            ) {
                Icon(
                    painter = painterResource(id = if(role == Role.BUYER) R.drawable.buyer else R.drawable.seller),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
            Column {
                Text(text = userContactData.username)
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.phone),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = userContactData.phoneNumber,
                        fontSize = screenFontSize(x = 14.0).sp,
//                            fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.email),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = userContactData.email,
                        fontSize = screenFontSize(x = 14.0).sp,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BusinessDetailsScreenPreview() {
    WazipayTheme {
        BusinessDetailsScreen(
            pullRefreshState = null,
            userId = 1,
            role = Role.BUYER,
            businessData = businessData,
            navigateToOrdersScreenWithArgs = {businessId ->  },
            navigateToCreateOrderScreenWithArgs = {businessId ->  },
            navigateToPreviousScreen = { /*TODO*/ },
            loadBusinessStatus = LoadBusinessStatus.INITIAL
        )
    }
}