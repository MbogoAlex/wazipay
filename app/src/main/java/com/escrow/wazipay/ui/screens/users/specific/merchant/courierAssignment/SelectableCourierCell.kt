package com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.user.UserDetailsData
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

@Composable
fun SelectableCourierCell(
    userDetailsData: UserDetailsData,
    showArrow: Boolean,
    navigateToCourierAssignmentScreen: (courierId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = screenWidth(x = 1.0),
                color = Color.LightGray,
                shape = RoundedCornerShape(screenWidth(x = 10.0))
            )
            .clickable {
                navigateToCourierAssignmentScreen(userDetailsData.userId.toString())
            }
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
                    painter = painterResource(id = R.drawable.motorbike),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
            Column {
                Text(text = userDetailsData.username)
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
                        text = userDetailsData.phoneNumber,
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
                        text = "mbogoalex3@gmail.com",
                        fontSize = screenFontSize(x = 14.0).sp,
                    )
                }
            }
            if(showArrow) {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        navigateToCourierAssignmentScreen(userDetailsData.userId.toString())
                    },
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Courier assignment screen"
                    )
                }
            }
        }
    }
}