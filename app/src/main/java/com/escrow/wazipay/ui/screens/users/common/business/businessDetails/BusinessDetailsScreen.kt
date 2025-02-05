package com.escrow.wazipay.ui.screens.users.common.business.businessDetails

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.AppViewModelFactory
import com.escrow.wazipay.R
import com.escrow.wazipay.data.network.models.business.BusinessData
import com.escrow.wazipay.data.network.models.business.businessData
import com.escrow.wazipay.data.network.models.user.UserContactData
import com.escrow.wazipay.data.room.models.Role
import com.escrow.wazipay.ui.nav.AppNavigation
import com.escrow.wazipay.ui.screens.users.common.business.LoadBusinessStatus
import com.escrow.wazipay.ui.theme.WazipayTheme
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth

object BusinessDetailsScreenDestination: AppNavigation {
    override val title: String = "Business details screen"
    override val route: String = "business-details-screen"
    val businessId: String = "businessId"
    val routeWithArgs: String = "$route/{$businessId}"
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BusinessDetailsScreenComposable(
    navigateToOrdersScreenWithArgs: (businessId: String) -> Unit,
    navigateToCreateOrderScreenWithArgs: (businessId: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: BusinessDetailsViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    var addProductExpanded by rememberSaveable { mutableStateOf(false) }
    var showEditProductPopup by rememberSaveable { mutableStateOf(false) }
    var showDeleteProductPopup by rememberSaveable { mutableStateOf(false) }
    var showDeleteBusinessPopup by rememberSaveable { mutableStateOf(false) }
    var showEditBusinessPopup by rememberSaveable { mutableStateOf(false) }

    var selectedProductId by rememberSaveable { mutableStateOf(-1) }
    var selectedProductName by rememberSaveable { mutableStateOf("") }

    if(uiState.editingStatus == EditingStatus.SUCCESS) {
        showEditBusinessPopup = false
        showEditProductPopup = false
        Toast.makeText(context, "Business updated", Toast.LENGTH_SHORT).show()
        viewModel.resetStatus()
    }

    if(uiState.archivingStatus == ArchivingStatus.SUCCESS) {
        showDeleteBusinessPopup = false
        Toast.makeText(context, "Business archived", Toast.LENGTH_SHORT).show()
        viewModel.resetStatus()
        navigateToPreviousScreen()
    }

    if(uiState.deletingStatus == DeletingStatus.SUCCESS) {
        showDeleteProductPopup = false
        Toast.makeText(context, "Product deleted", Toast.LENGTH_SHORT).show()
        viewModel.resetStatus()
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.loadBusinessStatus == LoadBusinessStatus.LOADING,
        onRefresh = {
            viewModel.getBusiness()

        }
    )

    if(showEditProductPopup) {
        EditProductPopup(
            productName = uiState.productName.ifEmpty { selectedProductName },
            onDismiss = { showEditProductPopup = !showEditProductPopup },
            onChangeName = viewModel::updateProductName,
            onConfirm = {
                viewModel.editProduct(productId = selectedProductId)
            },
            editingStatus = uiState.editingStatus
        )
    }

    if(showDeleteProductPopup) {
        DeleteProductDialog(
            productName = selectedProductName,
            onConfirm = {
                viewModel.deleteProduct(productId = selectedProductId)
            },
            onDismiss = {
                if(uiState.deletingStatus != DeletingStatus.LOADING) {
                    showDeleteProductPopup = !showDeleteProductPopup
                } else {}
            },
            deletingStatus = uiState.deletingStatus
        )
    }

    if(showDeleteBusinessPopup) {
        DeleteBusinessDialog(
            businessName = uiState.businessData.name,
            onConfirm = viewModel::archiveBusiness,
            onDismiss = {
                if(uiState.archivingStatus != ArchivingStatus.LOADING) {
                    showDeleteBusinessPopup = !showDeleteBusinessPopup
                } else {}
            },
            archivingStatus = uiState.archivingStatus
        )
    }

    if(showEditBusinessPopup) {
        EditBusinessPopup(
            businessData = uiState.businessData,
            businessName = uiState.businessName.ifEmpty { uiState.businessData.name },
            businessDescription = uiState.businessDescription.ifEmpty { uiState.businessData.description },
            location = uiState.location.ifEmpty { uiState.businessData.location },
            onDismiss = {
                if(uiState.editingStatus != EditingStatus.LOADING) {
                    showEditBusinessPopup = !showEditBusinessPopup
                } else {}
            },
            onChangeName = viewModel::updateBusinessName,
            onChangeDescription = viewModel::updateBusinessDescription,
            onChangeLocation = viewModel::updateBusinessLocation,
            onConfirm = viewModel::updateBusiness,
            editingStatus = uiState.editingStatus
        )
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        BusinessDetailsScreen(
            pullRefreshState = pullRefreshState,
            userId = uiState.userDetails.userId,
            role = uiState.role,
            businessData = uiState.businessData,
            addProductExpanded = addProductExpanded,
            onAddProduct = viewModel::addProduct,
            newProductName = uiState.newProductName,
            onChangeNewProductName = viewModel::updateNewProductName,
            onExpandAddProduct = {
                addProductExpanded = !addProductExpanded
            },
            onShowEditProductPopup = {id, name ->
                selectedProductId = id
                selectedProductName = name
                showEditProductPopup = !showEditProductPopup
            },
            onShowDeleteProductPopup = { id, name ->
                selectedProductId = id
                selectedProductName = name
                showDeleteProductPopup = !showDeleteProductPopup
            },
            onShowDeleteBusinessPopup = {
                showDeleteBusinessPopup = !showDeleteBusinessPopup
            },
            onShowEditBusinessPopup = {
                showEditBusinessPopup = !showEditBusinessPopup
            },
            navigateToOrdersScreenWithArgs = navigateToOrdersScreenWithArgs,
            navigateToCreateOrderScreenWithArgs = navigateToCreateOrderScreenWithArgs,
            navigateToPreviousScreen = navigateToPreviousScreen,
            loadBusinessStatus = uiState.loadBusinessStatus,
            editingStatus = uiState.editingStatus
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BusinessDetailsScreen(
    pullRefreshState: PullRefreshState?,
    userId: Int,
    role: Role,
    businessData: BusinessData,
    addProductExpanded: Boolean,
    newProductName: String,
    onChangeNewProductName: (name: String) -> Unit,
    onAddProduct: () -> Unit,
    onExpandAddProduct: () -> Unit,
    onShowEditProductPopup: (productId: Int, productName: String) -> Unit,
    onShowEditBusinessPopup: () -> Unit,
    onShowDeleteProductPopup: (productId: Int, productName: String) -> Unit,
    onShowDeleteBusinessPopup: () -> Unit,
    navigateToOrdersScreenWithArgs: (businessId: String) -> Unit,
    navigateToCreateOrderScreenWithArgs: (businessId: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    loadBusinessStatus: LoadBusinessStatus,
    editingStatus: EditingStatus,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = screenHeight(x = 16.0),
                horizontal = screenWidth(x = 16.0)
            )
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
            IconButton(onClick = navigateToPreviousScreen) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
            Text(
                text = "Business details",
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 16.0).sp
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = onShowDeleteBusinessPopup,
                modifier = Modifier
                    .padding(
                        screenWidth(x = 4.0)
                    )
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error)
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.onError,
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = "Delete business"
                )
            }
        }
        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
        if(loadBusinessStatus == LoadBusinessStatus.LOADING) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                PullRefreshIndicator(
                    refreshing = true,
                    state = pullRefreshState!!
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = businessData.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = screenFontSize(x = 16.0).sp
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Text(
                    text = businessData.description,
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = businessData.location,
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Text(
                    text = "Products",
                    fontWeight = FontWeight.Bold,
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                businessData.products.forEachIndexed { index, product ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.circle),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidth(x = 8.0))
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = product.name,
                                fontSize = screenFontSize(x = 14.0).sp,
                                modifier = Modifier
                                    .weight(1f)
                            )
                            IconButton(
                                onClick = {
                                    onShowEditProductPopup(product.productId, product.name)
                                },
                                modifier = Modifier
//                                    .background(MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(
                                    tint = MaterialTheme.colorScheme.primary,
                                    painter = painterResource(id = R.drawable.edit),
                                    contentDescription = "Edit product",
                                )
                            }
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            IconButton(
                                enabled = businessData.products.size > 1,
                                onClick = {
                                    onShowDeleteProductPopup(product.productId, product.name)
                                },
                                modifier = Modifier
//                                    .background(MaterialTheme.colorScheme.error)
                            ) {
                                Icon(
                                    tint = MaterialTheme.colorScheme.error,
                                    painter = painterResource(id = R.drawable.delete),
                                    contentDescription = "Delete product",
                                )
                            }
                        }

                    }
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                TextButton(
                    onClick = onExpandAddProduct,
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.add),
                            contentDescription = "Add product"
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Text(
                            text = "Add product",
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                    }
                }
                if(addProductExpanded) {
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    TextField(
                        label = {
                            Text(
                                text = "Product",
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        },
                        value = newProductName,
                        onValueChange = onChangeNewProductName,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        TextButton(onClick = onExpandAddProduct) {
                            Text(
                                text = "Cancel",
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        }
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Button(
                            enabled = editingStatus != EditingStatus.LOADING,
                            onClick = onAddProduct,
                        ) {
                            if(editingStatus == EditingStatus.LOADING) {
                                Text(
                                    text = "Loading...",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            } else if(editingStatus == EditingStatus.SUCCESS) {
                                onExpandAddProduct()
                                Toast.makeText(context, "Product added", Toast.LENGTH_SHORT).show()
                            } else {
                                Text(
                                    text = "Save",
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Text(
                    text = "About the merchant",
                    fontWeight = FontWeight.Bold,
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                ActorCard(
                    role = Role.MERCHANT,
                    userContactData = businessData.owner ?: UserContactData(0, "", "", "")
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onShowEditBusinessPopup,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Edit business",
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Icon(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "Edit business"
                )
            }
        }
    }
}

@Composable
fun ActorCard(
    role: Role,
    userContactData: UserContactData,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = screenWidth(x = 1.0),
                color = Color.LightGray,
                shape = RoundedCornerShape(screenWidth(x = 10.0))
            )
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
                    tint = MaterialTheme.colorScheme.onPrimary,
                    painter = painterResource(id = if(role == Role.BUYER) R.drawable.buyer else R.drawable.seller),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(screenWidth(x = 8.0)))
            Column {
                Text(
                    text = userContactData.username,
                    fontSize = screenFontSize(x = 14.0).sp
                )
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
                        text = userContactData.phoneNumber,
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
                        text = userContactData.email,
                        fontSize = screenFontSize(x = 14.0).sp,
                    )
                }
            }
        }
    }
}

@Composable
fun EditProductPopup(
    productName: String,
    onDismiss: () -> Unit,
    onChangeName: (name: String) -> Unit,
    onConfirm: () -> Unit,
    editingStatus: EditingStatus,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Edit product",
                fontSize = screenFontSize(x = 14.0).sp
            )
        },
        text = {
            Column {
                TextField(
                    label = {
                        Text(
                            text = "Product",
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                    },
                    value = productName,
                    onValueChange = onChangeName,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                enabled = editingStatus != EditingStatus.LOADING,
                onClick = onConfirm
            ) {
                if(editingStatus == EditingStatus.LOADING) {
                    Text(
                        text = "Loading...",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                } else {
                    Text(
                        text = "Confirm",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
            }
        },
        dismissButton = {
            TextButton(
                enabled = editingStatus != EditingStatus.LOADING,
                onClick = onDismiss
            ) {
                Text(
                    text = "Cancel",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    )
}

@Composable
fun EditBusinessPopup(
    businessName: String,
    businessDescription: String,
    businessData: BusinessData,
    location: String,
    onDismiss: () -> Unit,
    onChangeName: (name: String) -> Unit,
    onChangeDescription: (description: String) -> Unit,
    onChangeLocation: (location: String) -> Unit,
    onConfirm: () -> Unit,
    editingStatus: EditingStatus,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Edit business details",
                fontSize = screenFontSize(x = 14.0).sp
            )
        },
        text = {
            Column {
                TextField(
                    label = {
                        Text(
                            text = "Business name",
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                    },
                    value = businessName,
                    onValueChange = onChangeName,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                TextField(
                    label = {
                        Text(
                            text = "Business description",
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                    },
                    value = businessDescription,
                    onValueChange = onChangeDescription,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                TextField(
                    label = {
                        Text(
                            text = "Business location",
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                    },
                    value = location,
                    onValueChange = onChangeLocation,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                enabled = (businessName != businessData.name || businessDescription != businessData.description || businessData.location != location) && editingStatus != EditingStatus.LOADING,
                onClick = onConfirm
            ) {
                if(editingStatus == EditingStatus.LOADING) {
                    Text(
                        text = "Loading...",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                } else {
                    Text(
                        text = "Confirm",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }

            }
        },
        dismissButton = {
            TextButton(
                enabled = editingStatus != EditingStatus.LOADING,
                onClick = onDismiss
            ) {
                Text(
                    text = "Cancel",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    )
}

@Composable
fun DeleteProductDialog(
    productName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    deletingStatus: DeletingStatus,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Delete product",
                fontSize = screenFontSize(x = 14.0).sp
            )
        },
        text = {
            Text(
                text = "Are you sure you want to delete $productName?",
                fontSize = screenFontSize(x = 14.0).sp
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                enabled = deletingStatus != DeletingStatus.LOADING,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                ),
                onClick = onConfirm
            ) {
                if(deletingStatus == DeletingStatus.LOADING) {
                    Text(
                        text = "Loading...",
                        color = MaterialTheme.colorScheme.onError,
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                } else {
                    Text(
                        text = "Confirm",
                        color = MaterialTheme.colorScheme.onError,
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }

            }
        },
        dismissButton = {
            TextButton(
                enabled = deletingStatus != DeletingStatus.LOADING,
                onClick = onDismiss
            ) {
                Text(
                    text = "Cancel",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    )
}

@Composable
fun DeleteBusinessDialog(
    businessName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    archivingStatus: ArchivingStatus,
    modifier: Modifier = Modifier
) {
    var checkboxMarked by rememberSaveable {
        mutableStateOf(false)
    }
    AlertDialog(
        title = {
            Text(
                text = "Delete business",
                fontSize = screenFontSize(x = 14.0).sp
            )
        },
        text = {
            Column {
                Text(
                    text = "Are you sure you want to delete $businessName? This action cannot be undone.",
                    fontSize = screenFontSize(x = 14.0).sp
                )
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        checkboxMarked = !checkboxMarked
                    }) {
                        Icon(
                            painter = painterResource(id = if(checkboxMarked) R.drawable.check_box_marked else R.drawable.check_box_blank),
                            contentDescription = null
                        )
                    }
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = "I understand that this action cannot be undone.",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                enabled = checkboxMarked && archivingStatus != ArchivingStatus.LOADING,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                ),
                onClick = onConfirm
            ) {
                if(archivingStatus == ArchivingStatus.LOADING) {
                    Text(
                        text = "Loading...",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                } else {
                    Text(
                        text = "Confirm",
                        color = MaterialTheme.colorScheme.onError,
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
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

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BusinessDetailsScreenPreview() {
    WazipayTheme {
        var addProductExpanded by rememberSaveable { mutableStateOf(false) }
        var showEditProductPopup by rememberSaveable { mutableStateOf(false) }
        var showEditBusinessPopup by rememberSaveable { mutableStateOf(false) }

        BusinessDetailsScreen(
            pullRefreshState = null,
            userId = 1,
            role = Role.BUYER,
            businessData = businessData,
            addProductExpanded = addProductExpanded,
            onExpandAddProduct = {
                addProductExpanded = !addProductExpanded
            },
            onAddProduct = {},
            onShowEditProductPopup = { _, _ ->
                showEditProductPopup = !showEditProductPopup
            },
            onShowEditBusinessPopup = {
                showEditBusinessPopup = !showEditBusinessPopup
            },
            onShowDeleteBusinessPopup = {},
            newProductName = "",
            onChangeNewProductName = {},
            onShowDeleteProductPopup = {_ ,_ ->},
            navigateToOrdersScreenWithArgs = {businessId ->  },
            navigateToCreateOrderScreenWithArgs = {businessId ->  },
            navigateToPreviousScreen = { /*TODO*/ },
            loadBusinessStatus = LoadBusinessStatus.INITIAL,
            editingStatus = EditingStatus.INITIAL
        )
    }
}