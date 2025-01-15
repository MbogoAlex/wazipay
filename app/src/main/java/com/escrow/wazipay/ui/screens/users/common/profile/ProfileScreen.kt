package com.escrow.wazipay.ui.screens.users.common.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreenComposable(
    navigateToTransactionsScreen: () -> Unit,
    navigateToBusinessScreenWithOwnerId: (ownerId: String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {

    val viewModel: ProfileViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .safeDrawingPadding()
    ) {
        ProfileScreen(
            userId = uiState.userDetails.userId,
            verificationStatus = VerificationStatus.valueOf(uiState.userDetailsData.verificationStatus),
            role = uiState.role,
            navigateToTransactionsScreen = navigateToTransactionsScreen,
            navigateToBusinessScreenWithOwnerId = navigateToBusinessScreenWithOwnerId,
            onLogout = onLogout
        )
    }
}

@Composable
fun ProfileScreen(
    userId: Int,
    verificationStatus: VerificationStatus,
    role: Role,
    navigateToTransactionsScreen: () -> Unit,
    navigateToBusinessScreenWithOwnerId: (ownerId: String) -> Unit,
    onLogout: () -> Unit,
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
        if(verificationStatus == VerificationStatus.UNVERIFIED) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.user_details),
                        contentDescription = "Start verification"
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = "Start verification",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
            }
        } else if(verificationStatus == VerificationStatus.PENDING_VERIFICATION) {
            OutlinedButton(
                enabled = false,
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.user_details),
                        contentDescription = "Start verification"
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(text = verificationStatus.name.lowercase().replace("_", " ").replaceFirstChar { it.uppercase() })
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .border(
                        width = screenWidth(x = 1.0),
                        color = Color.LightGray,
                        shape = RoundedCornerShape(screenWidth(x = 16.0))
                    )
                    .align(Alignment.End)
            ) {
                Row(
                    modifier = Modifier
                        .padding(screenWidth(x = 8.0))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.user_details),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(text = verificationStatus.name.lowercase().replace("_", " ").replaceFirstChar { it.uppercase() })
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Icon(
                        painter = painterResource(id = R.drawable.verified),
                        contentDescription = "Verified"
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            ProfileCard(
                icon = R.drawable.user_details,
                title = "Account Overview",
                description = "View your account details, verification status, and personal information in one place.",
                onClick = { /*TODO*/ }
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            ProfileCard(
                icon = R.drawable.shop,
                title = "My Businesses",
                description = "Explore and manage the businesses you've added.",
                onClick = {
                    navigateToBusinessScreenWithOwnerId(userId.toString())
                }
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            ProfileCard(
                icon = R.drawable.transactions,
                title = "Transaction History",
                description = "Access a complete record of all your past transactions.",
                onClick = navigateToTransactionsScreen
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        TextButton(
            onClick = onLogout,
            modifier = Modifier
                .align(Alignment.End)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logout),
                    contentDescription = "Log out"
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Text(
                    text = "Log out",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    }
}


@Composable
fun BuyerProfileScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        ProfileCard(
            icon = R.drawable.user_details,
            title = "Account Overview",
            description = "Your account and verification details.",
            onClick = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        ProfileCard(
            icon = R.drawable.orders,
            title = "Recent orders",
            description = "Items you have ordered",
            onClick = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        ProfileCard(
            icon = R.drawable.invoice,
            title = "Recent payments (invoices)",
            description = "Payments you have made as a buyer and invoices you have received",
            onClick = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        ProfileCard(
            icon = R.drawable.transactions,
            title = "Recent transactions",
            description = "Wazipay wallet history and M-PESA transactions",
            onClick = { /*TODO*/ }
        )
    }
}

@Composable
fun MerchantProfileScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        ProfileCard(
            icon = R.drawable.user_details,
            title = "Account Overview",
            description = "Your account and verification details.",
            onClick = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        ProfileCard(
            icon = R.drawable.orders,
            title = "Recent orders",
            description = "Orders you have made / received as a merchant",
            onClick = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        ProfileCard(
            icon = R.drawable.invoice,
            title = "Recent payments (invoices)",
            description = "Payments you have made as a merchant and invoices you have issued",
            onClick = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        ProfileCard(
            icon = R.drawable.transactions,
            title = "Recent transactions",
            description = "Wazipay wallet history and M-PESA transactions",
            onClick = { /*TODO*/ }
        )
    }
}

@Composable
fun CourierProfileScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        ProfileCard(
            icon = R.drawable.user_details,
            title = "Account Overview",
            description = "Your account and verification details.",
            onClick = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        ProfileCard(
            icon = R.drawable.orders,
            title = "Recent orders",
            description = "Orders assigned to you for delivery",
            onClick = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        ProfileCard(
            icon = R.drawable.transactions,
            title = "Recent transactions",
            description = "Wazipay wallet history and M-PESA transactions",
            onClick = { /*TODO*/ }
        )
    }
}

@Composable
fun ProfileCard(
    icon: Int,
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .fillMaxWidth()
            .border(
                width = screenWidth(x = 1.0),
                color = Color.LightGray,
                shape = RoundedCornerShape(screenWidth(x = 16.0))
            )
            .padding(
                screenWidth(x = 16.0)
            )

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(
                        screenWidth(x = 16.0)
                    )
            )
            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = screenFontSize(x = 16.0).sp,
                    fontWeight = FontWeight.W500
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Text(
                    text = description,
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
            IconButton(
                onClick = onClick,
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    WazipayTheme {
        ProfileScreen(
            userId = 1,
            role = Role.BUYER,
            verificationStatus = VerificationStatus.UNVERIFIED,
            navigateToTransactionsScreen = {},
            navigateToBusinessScreenWithOwnerId = {},
            onLogout = {}
        )
    }
}