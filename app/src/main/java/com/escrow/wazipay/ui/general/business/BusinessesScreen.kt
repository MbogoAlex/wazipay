package com.escrow.wazipay.ui.general.business

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.business.businesses
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

@Composable
fun BusinessesScreenComposable(
    modifier: Modifier = Modifier
) {
    val viewModel: BusinessViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        BusinessesScreen(
            userId = uiState.userDetails.userId,
            businesses = uiState.businesses
        )
    }
}

@Composable
fun BusinessesScreen(
    userId: Int,
    businesses: List<BusinessData>,
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
        LazyColumn {
            items(businesses) {
                BusinessCellComposable(
                    userId = 1,
                    businessData = it
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun BusinessCellComposable(
    userId: Int,
    businessData: BusinessData,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(
                vertical = screenHeight(x = 8.0)
            )
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
            onClick = { /*TODO*/ },
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
fun BusinessScreenPreview() {
    WazipayTheme {
        BusinessesScreen(
            userId = 1,
            businesses = businesses
        )
    }
}