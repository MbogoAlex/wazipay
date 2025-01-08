package com.escrow.wazipay.ui.screens.users.common.business.businessDetails

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.business.businessData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.ui.nav.AppNavigation
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

    Box(
        modifier = Modifier
            .safeDrawingPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        BusinessDetailsScreen(
            userId = uiState.userDetails.userId,
            role = uiState.role,
            businessData = uiState.businessData,
            navigateToOrdersScreenWithArgs = navigateToOrdersScreenWithArgs,
            navigateToCreateOrderScreenWithArgs = navigateToCreateOrderScreenWithArgs,
            navigateToPreviousScreen = navigateToPreviousScreen
        )
    }
}

@Composable
fun BusinessDetailsScreen(
    userId: Int,
    role: Role,
    businessData: BusinessData,
    navigateToOrdersScreenWithArgs: (businessId: String) -> Unit,
    navigateToCreateOrderScreenWithArgs: (businessId: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
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
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
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
            Text(
                text = it,
                fontSize = screenFontSize(x = 14.0).sp
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = "About the seller",
            fontWeight = FontWeight.Bold,
            fontSize = screenFontSize(x = 14.0).sp
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = "Contact:",
            fontSize = screenFontSize(x = 14.0).sp
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = businessData.owner?.phoneNumber ?: "",
            fontSize = screenFontSize(x = 14.0).sp
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = "Email:",
            fontSize = screenFontSize(x = 14.0).sp
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = businessData.owner?.email ?: "",
            fontSize = screenFontSize(x = 14.0).sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = {
                navigateToOrdersScreenWithArgs(businessData.id.toString())
            }) {
                Text(
                    text = "My Orders",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {
                navigateToCreateOrderScreenWithArgs(businessData.id.toString())
            }) {
                if(role == Role.BUYER) {
                    Text(
                        text = "Pay business",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                } else {
                    Text(
                        text = "Create order",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BusinessDetailsScreenPreview() {
    WazipayTheme {
        BusinessDetailsScreen(
            userId = 1,
            role = Role.BUYER,
            businessData = businessData,
            navigateToOrdersScreenWithArgs = {businessId ->  },
            navigateToCreateOrderScreenWithArgs = {businessId ->  },
            navigateToPreviousScreen = { /*TODO*/ }
        )
    }
}