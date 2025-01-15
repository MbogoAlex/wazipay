package com.escrow.wazipay.ui.screens.users.common.profile

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.LoadingStatus
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

object UserAccountOverviewScreenDestination : AppNavigation {
    override val title: String = "User account overview"
    override val route: String = "user-account-overview-screen"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserAccountOverviewScreenComposable(
    navigateToLoginScreenWithArgs: (phoneNumber: String, password: String) -> Unit,
    navigateToUserVerificationScreen: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: UserAccountOverviewViewModel = viewModel(factory = AppViewModelFactory.Factory)
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

    var showUsernameDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var showEditLastNameDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var showEditEmailDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var logoutLoading by rememberSaveable {
        mutableStateOf(false)
    }

    var showLogoutDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

//    if(uiState.loadingStatus == LoadingStatus.SUCCESS) {
//        showEditLastNameDialog = false
//        showEditEmailDialog = false
//        Toast.makeText(context, uiState.successMessage, Toast.LENGTH_SHORT).show()
//        viewModel.resetLoadingStatus()
//    } else if(uiState.loadingStatus == LoadingStatus.FAIL) {
//        showEditLastNameDialog = false
//        showEditEmailDialog = false
//        Toast.makeText(context, uiState.successMessage, Toast.LENGTH_SHORT).show()
//        viewModel.resetLoadingStatus()
//    }

    if(showUsernameDialog) {
//        EditDialog(
//            heading = "Edit your first name",
//            label = "First name",
//            value = uiState.firstName,
//            onChangeValue = {
//                viewModel.updateFirstName(it)
//            },
//            onConfirm = {
//                viewModel.updateUserDetails()
//            },
//            onDismiss = {
//                if(uiState.loadingStatus != LoadingStatus.LOADING) {
//                    showUsernameDialog = !showUsernameDialog
//                }
//            },
//            loadingStatus = uiState.loadingStatus
//        )
    }

    if(showEditEmailDialog) {
//        EditDialog(
//            heading = "Edit your email",
//            label = "Email",
//            value = uiState.email,
//            onChangeValue = {
//                viewModel.updateEmail(it)
//            },
//            onConfirm = {
//                viewModel.updateUserDetails()
//            },
//            onDismiss = {
//                if(uiState.loadingStatus != LoadingStatus.LOADING) {
//                    showEditEmailDialog = !showEditEmailDialog
//                }
//            },
//            loadingStatus = uiState.loadingStatus
//        )
    }

    if(showLogoutDialog) {
        LogoutDialog(
            onConfirm = {
//                logoutLoading = true
//                val phoneNumber = uiState.userDetails.phoneNumber
//                val password = uiState.userDetails.password
//                showLogoutDialog = !showLogoutDialog
//                scope.launch {
//                    viewModel.logout()
//                    delay(2000)
//                    logoutLoading = !logoutLoading
//                    Toast.makeText(context, "Logging out", Toast.LENGTH_SHORT).show()
//                    try {
//                        navigateToLoginScreenWithArgs(phoneNumber, password)
//                    } catch (e: Exception) {
//                        Log.e("failedToLogout", e.toString())
//                    }
//                }
            },
            onDismiss = {
                showLogoutDialog = !showLogoutDialog
            }
        )
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        UserAccountOverviewScreen(
            verificationStatus =  if(uiState.userDetailsData.verificationStatus.isNotEmpty()) VerificationStatus.valueOf(uiState.userDetailsData.verificationStatus) else VerificationStatus.UNVERIFIED,
            onEditUsername = { /*TODO*/ },
            onEditLastName = { /*TODO*/ },
            onEditEmail = { /*TODO*/ },
            username = uiState.userDetails.username ?: "",
            phoneNumber = uiState.userDetails.phoneNumber ?: "",
            email = uiState.userDetails.email ?: "",
            logoutLoading = false,
            navigateToUserVerificationScreen = navigateToUserVerificationScreen,
            navigateToPreviousScreen = navigateToPreviousScreen,
            onLogout = {}
        )
    }
}

@Composable
fun UserAccountOverviewScreen(
    verificationStatus: VerificationStatus,
    onEditUsername: () -> Unit,
    onEditLastName: () -> Unit,
    onEditEmail: () -> Unit,
    username: String,
    email: String,
    phoneNumber: String,
    onLogout: () -> Unit,
    logoutLoading: Boolean,
    navigateToUserVerificationScreen: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .padding(
                vertical = screenHeight(x = 16.0),
                horizontal = screenWidth(x = 16.0)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = navigateToPreviousScreen
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous screen",
                    modifier = Modifier
                        .size(screenWidth(x = 24.0))
                )
            }
            Spacer(modifier = Modifier.width(screenWidth(x = 5.0)))
            Text(
                text = "Previous screen",
                fontSize = screenFontSize(x = 14.0).sp
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = screenWidth(x = 16.0),
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    enabled = false,
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                    )
                }
                Spacer(modifier = Modifier.width(screenWidth(x = 5.0)))
                Text(
                    text = "Account details",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            Text(
                text = "Username",
                fontSize = screenFontSize(x = 14.0).sp
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 10.0)))
            ElevatedCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(screenWidth(x = 20.0))
                        .fillMaxWidth()
                ) {
                    Text(
                        text = username,
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        tint = MaterialTheme.colorScheme.surfaceTint,
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                onEditUsername()
                            }
                            .size(screenWidth(x = 24.0))
                    )
                }
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 20.0)))
            Text(
                text = "Email",
                fontSize = screenFontSize(x = 14.0).sp
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 10.0)))
            ElevatedCard {
                Row(
                    modifier = Modifier
                        .padding(screenHeight(x = 20.0))
                        .fillMaxWidth()
                ) {
                    Text(
                        text = email,
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        tint = MaterialTheme.colorScheme.surfaceTint,
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                onEditEmail()
                            }
                            .size(screenWidth(x = 24.0))
                    )
                }
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 20.0)))
            Text(
                text = "Phone number",
                fontSize = screenFontSize(x = 14.0).sp
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 10.0)))
            ElevatedCard {
                Row(
                    modifier = Modifier
                        .padding(screenWidth(x = 20.0))
                        .fillMaxWidth()
                ) {
                    Text(
                        text = phoneNumber,
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        tint = Color.LightGray,
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier
                            .size(screenWidth(x = 24.0))
                    )
                }
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 20.0)))
            Row {
                Text(
                    text = "Verification status: ",
                    fontSize = screenFontSize(x = 14.0).sp,
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Text(
                    text = verificationStatus.name.lowercase().replace("_", " ").replaceFirstChar { it.uppercase() },
                    fontSize = screenFontSize(x = 14.0).sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 20.0)))
            Button(
                onClick = navigateToUserVerificationScreen,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Account verification",
                    fontSize = screenFontSize(x = 14.0).sp,
                )
            }
        }
    }

}

@Composable
fun EditDialog(
    heading: String,
    label: String,
    value: String,
    onChangeValue: (value: String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    loadingStatus: LoadingStatus,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = heading,
                fontSize = screenFontSize(x = 14.0).sp
            )
        },
        text = {
            OutlinedTextField(
                label = {
                    Text(
                        text = label,
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                },
                value = value,
                onValueChange = onChangeValue,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(
                enabled = loadingStatus != LoadingStatus.LOADING,
                onClick = onDismiss
            ) {
                Text(
                    text = "Dismiss",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        },
        confirmButton = {
            Button(
                enabled = value.isNotEmpty() && loadingStatus != LoadingStatus.LOADING,
                onClick = onConfirm
            ) {
                if(loadingStatus == LoadingStatus.LOADING) {
                    Text(
                        text = "Saving...",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                } else {
                    Text(
                        text = "Save",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
            }
        }
    )
}

@Composable
fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Logout",
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 14.0).sp
            )
        },
        text = {
            Text(
                text = "Are you sure you want to log out?",
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 14.0).sp
            )
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Dismiss",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm
            ) {
                Text(text = "Log out")
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UserAccountOverviewScreenPreview() {
    WazipayTheme {
        UserAccountOverviewScreen(
            verificationStatus = VerificationStatus.UNVERIFIED,
            onEditUsername = { /*TODO*/ },
            onEditLastName = { /*TODO*/ },
            onEditEmail = { /*TODO*/ },
            username = "",
            phoneNumber = "",
            email = "",
            logoutLoading = false,
            navigateToUserVerificationScreen = {},
            navigateToPreviousScreen = {},
            onLogout = {}
        )
    }
}