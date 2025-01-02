package com.escrow.wazipay.ui.buyer.invoice

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.business.businesses
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

@Composable
fun BusinessSelectionScreenComposable(
    modifier: Modifier = Modifier
) {

}

@Composable
fun BusinessSelectionScreen(
    searchQuery: String,
    onChangeSearchQuery: (text: String) -> Unit,
    onClearSearchQuery: () -> Unit,
    navigateToInvoiceCreationScreen: (businessId: String) -> Unit,
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
            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
            Text(
                text = "Create invoice",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Text(
            text = "Select a business",
            fontSize = screenFontSize(x = 14.0).sp,
            fontWeight = FontWeight.Bold
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
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        LazyColumn {
            items(businesses) {
                SelectableBusinessCell(
                    userId = 1,
                    businessData = it,
                    navigateToInvoiceCreationScreen = navigateToInvoiceCreationScreen
                )
            }
        }
    }
}

@Composable
fun SelectableBusinessCell(
    userId: Int,
    businessData: BusinessData,
    navigateToInvoiceCreationScreen: (businessId: String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                navigateToInvoiceCreationScreen(businessData.id.toString())
            }
    ) {
        Column(
            modifier = Modifier
                .padding(screenWidth(x = 8.0))
                .weight(1f)
        ) {
            if(businessData.owner.id == userId) {
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
                    painter = painterResource(id = R.drawable.shop),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Text(
                    text = businessData.name,
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Text(
                text = businessData.description,
                fontSize = screenFontSize(x = 14.0).sp,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 4.0)))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Text(text = businessData.owner.username)
                Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                Icon(
                    painter = painterResource(id = R.drawable.phone),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Text(text = businessData.owner.phoneNumber)
            }
        }
        IconButton(
            onClick = { navigateToInvoiceCreationScreen(businessData.id.toString()) },
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Business details"
            )
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
            searchQuery = "",
            onChangeSearchQuery = {},
            onClearSearchQuery = {},
            navigateToInvoiceCreationScreen = {},
            navigateToPreviousScreen = {}
        )
    }
}