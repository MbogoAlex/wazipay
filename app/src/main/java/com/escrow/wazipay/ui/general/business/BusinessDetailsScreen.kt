package com.escrow.wazipay.ui.general.business

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.business.businessData
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

@Composable
fun BusinessDetailsScreenComposable(
    navigateToOrdersScreenWithArgs: (userId: Int, businessId: Int) -> Unit,
    navigateToCreateOrderScreenWithArgs: (userId: Int, businessId: Int) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {

}

@Composable
fun BusinessDetailsScreen(
    userId: Int,
    businessData: BusinessData,
    navigateToOrdersScreenWithArgs: (userId: Int, businessId: Int) -> Unit,
    navigateToCreateOrderScreenWithArgs: (userId: Int, businessId: Int) -> Unit,
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
            text = businessData.owner.phoneNumber,
            fontSize = screenFontSize(x = 14.0).sp
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = "Email:",
            fontSize = screenFontSize(x = 14.0).sp
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = businessData.owner.email,
            fontSize = screenFontSize(x = 14.0).sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = { 
                navigateToOrdersScreenWithArgs(userId, businessData.id)
            }) {
                Text(
                    text = "My Orders",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {
                navigateToCreateOrderScreenWithArgs(userId, businessData.id)
            }) {
                Text(
                    text = "Create New Order",
                    fontSize = screenFontSize(x = 14.0).sp
                )
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
            businessData = businessData,
            navigateToOrdersScreenWithArgs = {userId, businessId ->  },
            navigateToCreateOrderScreenWithArgs = {userId, businessId ->  },
            navigateToPreviousScreen = { /*TODO*/ }
        )
    }
}