package com.escrow.wazipay.ui.screens.users.common.business.businessDetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.models.business.BusinessUpdateRequestBody
import com.escrow.wazipay.data.network.models.business.ProductUpdateRequestBody
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.repository.DBRepository
import com.escrow.wazipay.ui.screens.users.common.business.LoadBusinessStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BusinessDetailsViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _uiState = MutableStateFlow(BusinessDetailsUiData())
    val uiState: StateFlow<BusinessDetailsUiData> = _uiState.asStateFlow()

    fun updateProductName(name: String) {
        _uiState.update {
            it.copy(
                productName = name
            )
        }
    }

    fun updateNewProductName(name: String) {
        _uiState.update {
            it.copy(
                newProductName = name
            )
        }
    }

    fun updateBusinessName(name: String) {
        _uiState.update {
            it.copy(
                businessName = name
            )
        }
    }

    fun updateBusinessLocation(location: String) {
        _uiState.update {
            it.copy(
                location = location
            )
        }
    }

    fun updateBusinessDescription(desc: String) {
        _uiState.update {
            it.copy(
                businessDescription = desc
            )
        }
    }

    fun archiveBusiness() {
        _uiState.update {
            it.copy(
                archivingStatus = ArchivingStatus.LOADING
            )
        }

        viewModelScope.launch {
            try {
               val response = apiRepository.archiveBusiness(
                   token = uiState.value.userDetails.token!!,
                   businessId = uiState.value.businessId!!.toInt()
               )

               if(response.isSuccessful) {
                   _uiState.update {
                       it.copy(
                           archivingStatus = ArchivingStatus.SUCCESS
                       )
                   }
                   Log.d("businessArchive", "SUCCESS")
               } else {
                   _uiState.update {
                       it.copy(
                           archivingStatus = ArchivingStatus.FAIL
                       )
                   }
                   Log.e("businessArchive", "response: $response")
               }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        archivingStatus = ArchivingStatus.FAIL
                    )
                }
                Log.e("businessArchive", "exception: $e")
            }
        }
    }

    fun editProduct(productId: Int) {
        _uiState.update {
            it.copy(
                editingStatus = EditingStatus.LOADING
            )
        }

        viewModelScope.launch {
            try {
                val productUpdateRequestBody = ProductUpdateRequestBody(
                    productId = productId,
                    name = uiState.value.productName
                )
               val response = apiRepository.updateProduct(
                   token = uiState.value.userDetails.token!!,
                   productUpdateRequestBody = productUpdateRequestBody
               )

                if(response.isSuccessful) {
                    getBusiness()
                    _uiState.update {
                        it.copy(
                            editingStatus = EditingStatus.SUCCESS
                        )
                    }
                    Log.d("productUpdate", "SUCCESS")
                } else {
                    _uiState.update {
                        it.copy(
                            editingStatus = EditingStatus.FAIL
                        )
                    }
                    Log.e("productUpdate", "response: $response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        editingStatus = EditingStatus.FAIL
                    )
                }
                Log.e("productUpdate", "exception: $e")

            }
        }
    }

    fun addProduct() {
        _uiState.update {
            it.copy(
                editingStatus = EditingStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
                val products = listOf(uiState.value.newProductName)

                val businessUpdateRequestBody = BusinessUpdateRequestBody(
                    businessId = uiState.value.businessId!!.toInt(),
                    name = uiState.value.businessData.name,
                    description = uiState.value.businessData.description,
                    location = uiState.value.businessData.location,
                    products = products
                )
                val response = apiRepository.updateBusiness(
                    token = uiState.value.userDetails.token!!,
                    businessUpdateRequestBody = businessUpdateRequestBody
                )

                if(response.isSuccessful) {
                    getBusiness()
                    _uiState.update {
                        it.copy(
                            editingStatus = EditingStatus.SUCCESS
                        )
                    }
                    Log.d("productAddition", "SUCCESS")
                } else {
                    _uiState.update {
                        it.copy(
                            editingStatus = EditingStatus.FAIL
                        )
                    }
                    Log.e("productAddition", "response: $response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        editingStatus = EditingStatus.FAIL
                    )
                }
                Log.e("productAddition", "exception: $e")

            }
        }
    }

    fun deleteProduct(productId: Int) {
        _uiState.update {
            it.copy(
                deletingStatus = DeletingStatus.LOADING
            )
        }

        viewModelScope.launch {
            try {
               val response = apiRepository.deleteProduct(
                   token = uiState.value.userDetails.token!!,
                   productId = productId
               )

               if(response.isSuccessful) {
                   getBusiness()
                   _uiState.update {
                       it.copy(
                           deletingStatus = DeletingStatus.SUCCESS
                       )
                   }
                   Log.d("deleteProduct", "SUCCESS")
               } else {
                   _uiState.update {
                       it.copy(
                           deletingStatus = DeletingStatus.FAIL
                       )
                   }
                   Log.e("deleteProduct", "response: $response")
               }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        deletingStatus = DeletingStatus.FAIL
                    )
                }
                Log.e("deleteProduct", "exception: $e")
            }
        }
    }

    fun updateBusiness() {
        _uiState.update {
            it.copy(
                editingStatus = EditingStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {

                val businessUpdateRequestBody = BusinessUpdateRequestBody(
                    businessId = uiState.value.businessId!!.toInt(),
                    name = uiState.value.businessName,
                    description = uiState.value.businessDescription,
                    location = uiState.value.location,
                )
                val response = apiRepository.updateBusiness(
                    token = uiState.value.userDetails.token!!,
                    businessUpdateRequestBody = businessUpdateRequestBody
                )

                if(response.isSuccessful) {
                    getBusiness()
                    _uiState.update {
                        it.copy(
                            editingStatus = EditingStatus.SUCCESS
                        )
                    }
                    Log.d("productAddition", "SUCCESS")
                } else {
                    _uiState.update {
                        it.copy(
                            editingStatus = EditingStatus.FAIL
                        )
                    }
                    Log.e("productAddition", "response: $response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        editingStatus = EditingStatus.FAIL
                    )
                }
                Log.e("productAddition", "exception: $e")

            }
        }
    }

    fun getBusiness() {
//         _uiState.update {
//             it.copy(
//                 loadBusinessStatus = LoadBusinessStatus.LOADING
//             )
//         }
        viewModelScope.launch {
            try {
                val response = apiRepository.getBusiness(
                    token = uiState.value.userDetails.token!!,
                    businessId = if(uiState.value.businessId != null) uiState.value.businessId!!.toInt() else 1
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            businessData = response.body()?.data!!,
                            productName = response.body()?.data!!.name,
                            businessDescription = response.body()?.data!!.description,
                            location = response.body()?.data!!.location,
                            loadBusinessStatus = LoadBusinessStatus.SUCCESS
                        )
                    }
                } else {
                    if(response.code() == 401) {
                        _uiState.update {
                            it.copy(
                                unauthorized = true
                            )
                        }
                    }
                    _uiState.update {
                        it.copy(
                            loadBusinessStatus = LoadBusinessStatus.FAIL
                        )
                    }
                    Log.e("loadBusinessResponse_err", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadBusinessStatus = LoadBusinessStatus.FAIL
                    )
                }
                Log.e("loadBusinessException_err", e.toString())
            }
        }
    }

    private fun getUserDetails() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.getUsers().collect { users ->
                    _uiState.update {
                        it.copy(
                            userDetails = users[0]
                        )
                    }
                }
            }
        }
    }

    private fun getBusinessScreenData() {
        viewModelScope.launch {
            while (uiState.value.userDetails.userId == 0) {
                delay(1000)
            }
            getBusiness()
        }
    }

    private fun getUserRole() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.getUserRole().collect { userRole ->
                    _uiState.update {
                        it.copy(
                            role = userRole!!.role
                        )
                    }
                }
            }
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                loadBusinessStatus = LoadBusinessStatus.INITIAL,
                deletingStatus = DeletingStatus.INITIAL,
                editingStatus = EditingStatus.INITIAL,
                archivingStatus = ArchivingStatus.INITIAL
            )
        }
    }

    init {
        _uiState.update {
            it.copy(
                businessId = savedStateHandle[BusinessDetailsScreenDestination.businessId]
            )
        }
        getUserRole()
        getUserDetails()
        getBusinessScreenData()
    }
}