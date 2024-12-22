package com.escrow.wazipay.data.network.repository

import com.escrow.wazipay.data.network.models.common.LoginRequestBody
import com.escrow.wazipay.data.network.models.common.LoginResponseBody
import com.escrow.wazipay.data.network.models.common.RegistrationRequestBody
import com.escrow.wazipay.data.network.models.common.RegistrationResponseBody
import com.escrow.wazipay.data.network.models.common.SetPinRequestBody
import com.escrow.wazipay.data.network.models.common.SetPinResponseBody
import retrofit2.Response

class ApiRepositoryImpl(private val apiService: ApiService): ApiRepository {
    override suspend fun registerUser(registrationRequestBody: RegistrationRequestBody): Response<RegistrationResponseBody> =
        apiService.registerUser(registrationRequestBody)

    override suspend fun login(loginRequestBody: LoginRequestBody): Response<LoginResponseBody> =
        apiService.login(loginRequestBody)

    override suspend fun setUserPin(setPinRequestBody: SetPinRequestBody): Response<SetPinResponseBody> =
        apiService.setUserPin(setPinRequestBody)
}