package com.escrow.wazipay.ui.general.business

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.business.businesses
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BusinessesScreenComposable(
    navigateToBusinessDetailsScreen: (businessId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: BusinessViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        BusinessesScreen(
            role = uiState.userRole.role,
            searchQuery = uiState.searchQuery ?: "",
            onChangeSearchQuery = viewModel::updateSearchQuery,
            onClearSearchQuery = {
                viewModel.updateSearchQuery(null)
            },
            userId = uiState.userDetails.userId,
            businesses = uiState.businesses,
            navigateToBusinessDetailsScreen = navigateToBusinessDetailsScreen
        )
    }
}

@Composable
fun BusinessesScreen(
    role: Role,
    searchQuery: String,
    onChangeSearchQuery: (query: String) -> Unit,
    onClearSearchQuery: () -> Unit,
    userId: Int,
    businesses: List<BusinessData>,
    navigateToBusinessDetailsScreen: (businessId: String) -> Unit,
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
        Text(
            text = if(role == Role.BUYER) "Businesses" else if(role == Role.MERCHANT) "My Businesses" else "Businesses",
            fontWeight = FontWeight.Bold,
            fontSize = screenFontSize(x = 16.0).sp
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
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        LazyColumn {
            items(businesses) {
                BusinessCellComposable(
                    userId = userId,
                    businessData = it,
                    navigateToBusinessDetailsScreen = navigateToBusinessDetailsScreen
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BusinessScreenPreview() {
    WazipayTheme {
        BusinessesScreen(
            role = Role.BUYER,
            searchQuery = "",
            onClearSearchQuery = {},
            onChangeSearchQuery = {},
            userId = 1,
            businesses = businesses,
            navigateToBusinessDetailsScreen = {}
        )
    }
}