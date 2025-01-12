package com.escrow.wazipay.ui.screens.users.common.wallet

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.escrow.wazipay.R
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

@Composable
fun WalletCard(
    walletExpanded: Boolean,
    role: Role,
    username: String,
    userVerified: Boolean,
    walletBalance: String,
    navigateToDepositScreen: () -> Unit,
    navigateToWithdrawalScreen: () -> Unit,
    onExpandWallet: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(screenWidth(x = 16.0))
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = null
                )
                Text(
                    text = username.split(" ")[0].uppercase(),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                ElevatedCard {
                    Text(
                        text = if(userVerified) "VERIFIED" else "UNVERIFIED",
                        fontSize = screenFontSize(x = 12.0).sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(screenWidth(x = 3.0))
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                when(role) {
                    Role.BUYER -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "BUYER",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Icon(
                                painter = painterResource(id = R.drawable.buyer),
                                contentDescription = null
                            )
                        }
                    }
                    Role.MERCHANT -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "MERCHANT",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Icon(
                                painter = painterResource(id = R.drawable.seller),
                                contentDescription = null
                            )
                        }
                    }
                    Role.COURIER -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "COURIER",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Icon(
                                painter = painterResource(id = R.drawable.motorbike),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Text(
                text = "Wallet Balance",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = screenFontSize(x = 14.0).sp
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            if(!walletExpanded) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clickable {
                            onExpandWallet()
                        }
                        .fillMaxWidth()
                ) {
                    Text(text = "Click to expand")
                    Icon(
                        painter = painterResource(id = R.drawable.double_arrow_right),
                        contentDescription = "Expand wallet"
                    )
                }
            }
            if(walletExpanded) {
                Column {
                    Text(
                        text = walletBalance,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = screenFontSize(x = 24.0).sp
                    )
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = {
                            navigateToDepositScreen()
                        }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "+",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                                Text(
                                    text = "Deposit",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
                        OutlinedButton(onClick = {
                            navigateToWithdrawalScreen()
                        }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "-",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                                Text(
                                    text = "Withdraw",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Hide Balance",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Switch(checked = false, onCheckedChange = {})
                    }
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    if(walletExpanded) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .clickable {
                                    onExpandWallet()
                                }
                                .fillMaxWidth()
                        ) {
                            Text(text = "Click to collapse")
                            Icon(
                                painter = painterResource(id = R.drawable.double_arrow_left),
                                contentDescription = "Collapse wallet"
                            )
                        }
                    }
                }
            }

        }
    }
}