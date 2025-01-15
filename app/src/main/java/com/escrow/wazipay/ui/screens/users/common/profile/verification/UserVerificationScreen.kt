package com.escrow.wazipay.ui.screens.users.common.profile.verification

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.escrow.wazipay.R
import com.escrow.wazipay.ui.screens.users.common.profile.VerificationStatus
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.LoadingStatus
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

@Composable
fun UserVerificationScreenComposable(
    modifier: Modifier = Modifier
) {

}

@Composable
fun UserVerificationScreen(
    verificationStatus: VerificationStatus,
    frontIdUri: Uri?,
    backIdUri: Uri?,
    removeFrontPhoto: () -> Unit,
    onIdFrontUpload: () -> Unit,
    removeBackPhoto: () -> Unit,
    onIdBackUpload: () -> Unit,
    buttonEnabled: Boolean,
    loadingStatus: LoadingStatus,
    onUploadDocuments: () -> Unit,
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
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = screenWidth(x = 16.0))
        ) {
            IconButton(onClick = navigateToPreviousScreen) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Text(
                text = "User verification",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = screenFontSize(x = 16.0).sp
            )
        }
        when(verificationStatus) {
            VerificationStatus.UNVERIFIED -> {
                Text(
                    text = "Upload your documents for verification",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
            VerificationStatus.PENDING_VERIFICATION ->{
                Text(
                    text = "Your documents are being verified",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
            VerificationStatus.VERIFIED -> {
                Text(
                    text = "Your documents have been verified",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
        Column(
            modifier = Modifier
                .weight(10f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "ID Front:",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            if(frontIdUri != null) {
                Box {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(frontIdUri)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Front ID",
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                    )
                    IconButton(
                        modifier = Modifier
                            .alpha(0.5f)
                            .background(Color.Black)
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                            )
                            .align(Alignment.TopEnd),
                        onClick = removeFrontPhoto
                    ) {
                        Icon(
                            tint = Color.White,
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Remove front id"
                        )
                    }
                }
            } else {
                IdUpload(
                    onIdUpload = onIdFrontUpload,
                    uploadText = "Upload ID front",
                    imageUri = frontIdUri,
                    modifier = Modifier
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "ID Back:",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            if(backIdUri != null) {
                Box {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(backIdUri)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Front ID",
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                    )
                    IconButton(
                        modifier = Modifier
                            .alpha(0.5f)
                            .background(Color.Black)
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                            )
                            .align(Alignment.TopEnd),
                        onClick = removeBackPhoto
                    ) {
                        Icon(
                            tint = Color.White,
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Remove back id"
                        )
                    }
                }
            } else {
                IdUpload(
                    onIdUpload = onIdBackUpload,
                    uploadText = "Upload ID back",
                    imageUri = backIdUri,
                    modifier = Modifier
                )
            }


        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            enabled = buttonEnabled && loadingStatus != LoadingStatus.LOADING,
            modifier = Modifier
                .fillMaxWidth(),
            onClick = onUploadDocuments
        ) {
            if(loadingStatus == LoadingStatus.LOADING) {
                Text(
                    text = "Loading...",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            } else {
                Text(
                    text = "Upload ID photos",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    }
}

@Composable
fun IdUpload(
    onIdUpload: () -> Unit,
    uploadText: String,
    imageUri: Uri?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            IconButton(onClick = onIdUpload) {
                Icon(
                    painter = painterResource(id = R.drawable.upload),
                    contentDescription = uploadText
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UserVerificationScreenPreview() {
    WazipayTheme {
        UserVerificationScreen(
            verificationStatus = VerificationStatus.UNVERIFIED,
            frontIdUri = null,
            backIdUri = null,
            removeFrontPhoto = { /*TODO*/ },
            onIdFrontUpload = { /*TODO*/ },
            removeBackPhoto = { /*TODO*/ },
            onIdBackUpload = { /*TODO*/ },
            buttonEnabled = false,
            loadingStatus = LoadingStatus.INITIAL,
            onUploadDocuments = { /*TODO*/ },
            navigateToPreviousScreen = { /*TODO*/ }
        )
    }
}