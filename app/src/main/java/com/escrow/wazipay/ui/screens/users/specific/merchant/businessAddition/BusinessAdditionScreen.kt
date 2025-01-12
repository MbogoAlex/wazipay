package com.escrow.wazipay.ui.screens.users.specific.merchant.businessAddition

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.R
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.LoadingStatus
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BusinessAdditionScreenComposable(
    navigateToBusinessDetailsScreen: (businessId: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: BusinessAdditionViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    var showConfirmationDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var showSuccessDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if(uiState.loadingStatus == LoadingStatus.SUCCESS) {
        showSuccessDialog = true
    }

    if(showConfirmationDialog) {
        BusinessAdditionConfirmationDialog(
            onConfirm = {
                showConfirmationDialog = !showConfirmationDialog
                viewModel.addBusiness()
            },
            onDismiss = { showConfirmationDialog = !showConfirmationDialog },
            businessName = uiState.name
        )
    }

    if(showSuccessDialog) {
        BusinessAdditionSuccessDialog(
            onConfirm = {
                showConfirmationDialog = !showConfirmationDialog
                viewModel.resetStatus()
                navigateToBusinessDetailsScreen(uiState.businessId.toString())
            },
            onDismiss = {
                showConfirmationDialog = !showConfirmationDialog
                viewModel.resetStatus()
                navigateToBusinessDetailsScreen(uiState.businessId.toString())
            },
            businessName = uiState.name
        )
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        BusinessAdditionScreen(
            name = uiState.name,
            onChangeBusinessName = {
                viewModel.changeName(it)
                viewModel.enableButton()
            },
            description = uiState.description,
            onChangeBusinessDescription = {
                viewModel.changeDescription(it)
                viewModel.enableButton()
            },
            location = uiState.location,
            onChangeBusinessLocation = {
                viewModel.changeLocation(it)
                viewModel.enableButton()
            },
            products = uiState.products,
            onAddProductField = viewModel::addProductField,
            onRemoveProductField = {
                viewModel.removeProductField(it)
            },
            onChangeProduct = {product, index ->
                viewModel.changeProduct(product, index)
            },
            onAddBusiness = { showConfirmationDialog =! showConfirmationDialog },
            buttonEnabled = uiState.buttonEnabled,
            loadingStatus = uiState.loadingStatus,
            navigateToPreviousScreen = navigateToPreviousScreen
        )
    }

}

@Composable
fun BusinessAdditionScreen(
    name: String,
    onChangeBusinessName: (name: String) -> Unit,
    description: String,
    onChangeBusinessDescription: (description: String) -> Unit,
    location: String,
    onChangeBusinessLocation: (location: String) -> Unit,
    products: List<String>,
    onAddProductField: () -> Unit,
    onRemoveProductField: (index: Int) -> Unit,
    onChangeProduct: (product: String, index: Int) -> Unit,
    onAddBusiness: () -> Unit,
    buttonEnabled: Boolean,
    loadingStatus: LoadingStatus,
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
            IconButton(
                enabled = loadingStatus != LoadingStatus.LOADING,
                onClick = navigateToPreviousScreen
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
            Text(
                text = "Add new business",
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 16.0).sp
            )
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Business details",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            TextField(
                shape = RoundedCornerShape(screenWidth(x = 10.0)),
                value = name,
                label = {
                    Text(
                        text = "Business name",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = onChangeBusinessName,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            TextField(
                shape = RoundedCornerShape(screenWidth(x = 10.0)),
                value = description,
                label = {
                    Text(
                        text = "Business description (What you do)",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = onChangeBusinessDescription,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            TextField(
                shape = RoundedCornerShape(screenWidth(x = 10.0)),
                value = location,
                label = {
                    Text(
                        text = "Business location",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = onChangeBusinessLocation,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            Text(
                text = "Add Business products",
                fontSize = screenFontSize(x = 14.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            products.forEachIndexed { index, s ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ProductTextFieldCell(
                        value = products[index],
                        label = "Product ${index + 1}",
                        onChangeValue = {
                            onChangeProduct(it, index)
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                    )
                    IconButton(
                        enabled = index != 0,
                        onClick = { onRemoveProductField(index) }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.remove),
                            contentDescription = "Remove product"
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            IconButton(onClick = onAddProductField) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "Add product"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                enabled = buttonEnabled && loadingStatus != LoadingStatus.LOADING,
                onClick = onAddBusiness,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if(loadingStatus == LoadingStatus.LOADING) {
                    Text(
                        text = "Loading...",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                } else {
                    Text(
                        text = "Add business",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
            }
        }
    }
}

@Composable
fun ProductTextFieldCell(
    value: String,
    label: String,
    onChangeValue: (value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        shape = RoundedCornerShape(screenWidth(x = 10.0)),
        value = value,
        label = {
            Text(
                text = label,
                fontSize = screenFontSize(x = 14.0).sp
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        onValueChange = onChangeValue,
        modifier = modifier
            .fillMaxWidth()
    )
}

@Composable
fun BusinessAdditionConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    businessName: String,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Confirm business addition",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Are you sure you want to add $businessName to your profile?",
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
fun BusinessAdditionSuccessDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    businessName: String,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Business addition successful",
                fontSize = screenFontSize(x = 16.0).sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "$businessName has been added to your profile",
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
fun BusinessAdditionScreenPreview() {
    WazipayTheme {
        BusinessAdditionScreen(
            name = "",
            onChangeBusinessName = {},
            description = "",
            onChangeBusinessDescription = {},
            location = "",
            onChangeBusinessLocation = {},
            products = listOf(""),
            onAddProductField = {},
            onRemoveProductField = {},
            onChangeProduct = {product, index ->  },
            buttonEnabled = false,
            loadingStatus = LoadingStatus.INITIAL,
            onAddBusiness = {},
            navigateToPreviousScreen = {}
        )
    }
}