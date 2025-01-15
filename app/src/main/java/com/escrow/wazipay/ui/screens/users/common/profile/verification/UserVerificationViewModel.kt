package com.escrow.wazipay.ui.screens.users.common.profile.verification

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.models.UserDetails
import com.escrow.wazipay.data.room.repository.DBRepository
import com.escrow.wazipay.ui.screens.users.common.profile.VerificationStatus
import com.escrow.wazipay.ui.screens.users.specific.merchant.courierAssignment.LoadingStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.FileInputStream

class UserVerificationViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(UserVerificationUiData())
    val uiState: StateFlow<UserVerificationUiData> = _uiState.asStateFlow()

    fun uploadFrontPart(image: Uri?) {
        _uiState.update {
            it.copy(
                idFront = image,
            )
        }
    }

    fun uploadBackPart(image: Uri?) {
        _uiState.update {
            it.copy(
                idBack = image,
            )
        }
    }

    fun uploadId(context: Context) {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }

        viewModelScope.launch {
            val documents = listOf(uiState.value.idFront, uiState.value.idBack)
            val imageParts = ArrayList<MultipartBody.Part>()
            documents.forEach { uri ->
                val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri!!, "r", null)
                parcelFileDescriptor?.let { pfd ->
                    val inputStream = FileInputStream(pfd.fileDescriptor)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    val buffer = ByteArray(1024)
                    var length: Int
                    while (inputStream.read(buffer).also { length = it } != -1) {
                        byteArrayOutputStream.write(buffer, 0, length)
                    }
                    val byteArray = byteArrayOutputStream.toByteArray()

                    //Get the MIME type of the file

                    val mimeType = context.contentResolver.getType(uri)
                    val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                    val requestFile = RequestBody.create(mimeType?.toMediaTypeOrNull(), byteArray)
                    val imagePart = MultipartBody.Part.createFormData("files", "upload.$extension", requestFile)
                    imageParts.add(imagePart)
                }
            }

            try {
               val response = apiRepository.requestUserVerification(
                   token = uiState.value.userDetails.token!!,
                   files = imageParts
               )

               if(response.isSuccessful) {
                   _uiState.update {
                       it.copy(
                           loadingStatus = LoadingStatus.SUCCESS
                       )
                   }
               } else {
                   _uiState.update {
                       it.copy(
                           loadingStatus = LoadingStatus.FAIL
                       )
                   }

                   Log.e("uploadVerificationDocumentsResponse_Err", response.toString())
               }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }
                Log.e("uploadVerificationDocumentsException_Err", e.toString())
            }
        }
    }

    private fun getUserDetails() {
        viewModelScope.launch {
            try {
                while(uiState.value.userDetails.userId == 0) {
                    delay(1000)
                }
                val response = apiRepository.getUserDetails(
                    token = uiState.value.userDetails.token!!,
                    userId = uiState.value.userDetails.userId
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            userDetailsData = response.body()?.data!!,
                            idBack = if(response.body()?.data!!.idBack != null) Uri.parse(response.body()?.data!!.idBack) else null,
                            idFront = if(response.body()?.data!!.idFront != null) Uri.parse(response.body()?.data!!.idFront) else null
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

                    Log.e("getUserResponse_err", response.toString())
                }

            } catch (e: Exception) {

                Log.e("getUserException_err", e.toString())
            }
        }
    }

    private fun loadUserDetails() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.getUsers().collect { users ->
                    _uiState.update {
                        it.copy(
                            userDetails = if(users.isNotEmpty()) users[0] else UserDetails()
                        )
                    }
                }
            }
        }
    }

    fun loadVerificationScreenUiData() {
        viewModelScope.launch {
            while (uiState.value.userDetails.userId == 0) {
                delay(1000)
            }
            getUserDetails()
        }
    }

    fun buttonEnabled() {
        _uiState.update {
            it.copy(
                buttonEnabled = VerificationStatus.valueOf(uiState.value.userDetailsData.verificationStatus) != VerificationStatus.PENDING_VERIFICATION &&
                VerificationStatus.valueOf(uiState.value.userDetailsData.verificationStatus) != VerificationStatus.VERIFIED &&
                uiState.value.idFront != null &&
                uiState.value.idBack != null
            )
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.INITIAL
            )
        }
    }

    init {
        loadUserDetails()
        loadVerificationScreenUiData()
    }
}