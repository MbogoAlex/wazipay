package com.escrow.wazipay.ui.screens.users.specific.buyer.businessPayment

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.business.businesses
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

object BusinessSelectionScreenDestination: AppNavigation {
    override val title: String = "Business selection screen"
    override val route: String = "business-selection-screen"
    val toBuyerSelectionScreen: String = "toBuyerSelectionScreen"
    val routeWithArgs = "$route/{$toBuyerSelectionScreen}"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BusinessSelectionScreenComposable(
    navigateToInvoiceCreationScreen: (businessId: String) -> Unit,
    navigateToBuyerSelectionScreen: (businessId: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    showBackArrow: Boolean = true,
    modifier: Modifier = Modifier
) {
    val viewModel: BusinessSelectionViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .safeDrawingPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        BusinessSelectionScreen(
            userId = uiState.userDetails.userId,
            toBuyerSelectionScreen = uiState.toBuyerSelectionScreen,
            searchQuery = uiState.searchQuery ?: "",
            onChangeSearchQuery = {
                viewModel.changeSearchText(it)
            },
            onClearSearchQuery = {
                viewModel.changeSearchText(null)
            },
            businesses = uiState.businesses,
            navigateToInvoiceCreationScreen = navigateToInvoiceCreationScreen,
            navigateToBuyerSelectionScreen = navigateToBuyerSelectionScreen,
            navigateToPreviousScreen = navigateToPreviousScreen,
            showBackArrow = showBackArrow
        )
    }
}

@Composable
fun BusinessSelectionScreen(
    userId: Int,
    toBuyerSelectionScreen: Boolean,
    searchQuery: String,
    onChangeSearchQuery: (text: String) -> Unit,
    onClearSearchQuery: () -> Unit,
    businesses: List<BusinessData>,
    navigateToInvoiceCreationScreen: (businessId: String) -> Unit,
    navigateToBuyerSelectionScreen: (businessId: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    showBackArrow: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = if(showBackArrow) Modifier
            .fillMaxSize()
            .padding(
                vertical = screenHeight(x = 16.0),
                horizontal = screenWidth(x = 16.0)
            ) else Modifier.padding(
            start = screenWidth(x = 16.0),
            end = screenWidth(x = 16.0),
            bottom = screenHeight(x = 16.0)
        )
    ) {
        if(showBackArrow) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = navigateToPreviousScreen) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onBackground,
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous screen"
                    )
                }
                Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                if(toBuyerSelectionScreen) {
                    Text(
                        text = "Invoice issuance",
                        fontSize = screenFontSize(x = 16.0).sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                } else {
                    Text(
                        text = "Business payment",
                        fontSize = screenFontSize(x = 16.0).sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

            }
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        }
        Text(
            text = "Search and select business",
            fontSize = screenFontSize(x = 14.0).sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        TextField(
            shape = RoundedCornerShape(screenWidth(x = 10.0)),
//            leadingIcon = {
//                IconButton(onClick = { /*TODO*/ }) {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                        contentDescription = "Back"
//                    )
//                }
//            },
            label = {
                if(toBuyerSelectionScreen) {
                    Text(
                        text = "Search your business",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                } else {
                    Text(
                        text = "Search business / owner",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
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
        if(businesses.isEmpty()) {
            if(toBuyerSelectionScreen) {
                Text(
                    text = "You have no business yet",
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
        LazyColumn {
            items(businesses) {
                SelectableBusinessCell(
                    userId = userId,
                    businessData = it,
                    navigateToNextScreen = {businessId ->
                        if(toBuyerSelectionScreen) {
                            Log.d("navigatingWithBusinessArgs", businessId)
                            navigateToBuyerSelectionScreen(businessId)
                        } else {
                            navigateToInvoiceCreationScreen(businessId)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            }
        }
    }
}

@Composable
fun SelectableBusinessCell(
    userId: Int,
    businessData: BusinessData,
    navigateToNextScreen: (businessId: String) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = screenWidth(x = 1.0),
                color = Color.LightGray,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    navigateToNextScreen(businessData.id.toString())
                }
        ) {
            Column(
                modifier = Modifier
                    .padding(screenWidth(x = 8.0))
                    .weight(1f)
            ) {
                if(businessData.owner?.id == userId) {
                    Text(
                        text = "My Business",
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(screenWidth(x = 4.0)))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onBackground,
                        painter = painterResource(id = R.drawable.shop),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = businessData.name,
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Text(
                    text = businessData.description,
                    fontSize = screenFontSize(x = 14.0).sp,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 4.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onBackground,
                        painter = painterResource(id = R.drawable.person),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = businessData.owner?.username ?: "",
                        fontSize = screenFontSize(x = 14.0).sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                    Icon(
                        tint = MaterialTheme.colorScheme.onBackground,
                        painter = painterResource(id = R.drawable.phone),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = businessData.owner?.phoneNumber ?: "",
                        fontSize = screenFontSize(x = 14.0).sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            IconButton(
                onClick = { navigateToNextScreen(businessData.id.toString()) },
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.onBackground,
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Business details"
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BusinessSelectionScreenPreview(
    modifier: Modifier = Modifier
) {
    WazipayTheme {
        BusinessSelectionScreen(
            userId = 1,
            toBuyerSelectionScreen = false,
            searchQuery = "",
            businesses = businesses,
            onChangeSearchQuery = {},
            onClearSearchQuery = {},
            navigateToInvoiceCreationScreen = {},
            navigateToBuyerSelectionScreen = {},
            navigateToPreviousScreen = {},
            showBackArrow = true
        )
    }
}