package com.escrow.wazipay.ui.screens.users.common.profile.verification

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.R
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.screens.users.common.profile.VerificationStatus
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.LoadingStatus
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

object UserVerificationScreenDestination: AppNavigation {
    override val title: String = "User verification screen"
    override val route: String = "user-verification-screen"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserVerificationScreenComposable(
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: UserVerificationViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        when(lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> {
                viewModel.loadVerificationScreenUiData()
            }
        }
    }

    var showConfirmDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var showSuccessDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if(uiState.loadingStatus == LoadingStatus.SUCCESS) {
        showSuccessDialog = true
    }

    if(showConfirmDialog) {
        IdUploadConfirmationDialog(
            onConfirm = {
                showConfirmDialog = !showConfirmDialog
                viewModel.uploadId(context)
            },
            onDismiss = {
                showConfirmDialog = !showConfirmDialog

            }
        )
    }

    if(showSuccessDialog) {
        IdUploadSuccessDialog(
            onConfirm = {
                viewModel.loadVerificationScreenUiData()
                viewModel.resetStatus()
                showSuccessDialog = !showSuccessDialog
            },
            onDismiss = {
                viewModel.loadVerificationScreenUiData()
                viewModel.resetStatus()
                showSuccessDialog = !showSuccessDialog
            }
        )
    }

    val frontPartUpload = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {uri ->
            if(uri != null) {
                viewModel.uploadFrontPart(uri)
                viewModel.buttonEnabled()
            }
        }
    )

    val backPartUpload = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {uri ->
            if(uri != null) {
                viewModel.uploadBackPart(uri)
                viewModel.buttonEnabled()
            }
        }
    )

    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        UserVerificationScreen(
            verificationStatus =  if(uiState.userDetailsData.verificationStatus.isNotEmpty()) VerificationStatus.valueOf(uiState.userDetailsData.verificationStatus) else VerificationStatus.UNVERIFIED,
            frontIdUri = uiState.idFront,
            backIdUri = uiState.idBack,
            removeFrontPhoto = { /*TODO*/ },
            onIdFrontUpload = {
                frontPartUpload.launch("image/*")
            },
            removeBackPhoto = { /*TODO*/ },
            onIdBackUpload = {
                backPartUpload.launch("image/*")
            },
            buttonEnabled = uiState.buttonEnabled,
            loadingStatus = uiState.loadingStatus,
            onUploadDocuments = {
                showConfirmDialog = !showConfirmDialog
            },
            navigateToPreviousScreen = navigateToPreviousScreen
        )
    }
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

@Composable
fun IdUploadConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Upload confirmation",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Are you sure you want to upload your ID photos? Make sure they are clear enough before clicking confirm",
                fontSize = screenFontSize(x = 14.0).sp
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(
                    text = "Confirm",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    )
}

@Composable
fun IdUploadSuccessDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Upload success",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Your ID photos have been uploaded successfully. Your verification status will be updated soon",
                fontSize = screenFontSize(x = 14.0).sp
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(
                    text = "Done",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        },
    )
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