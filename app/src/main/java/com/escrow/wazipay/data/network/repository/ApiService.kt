package com.escrow.wazipay.data.network.repository

import com.escrow.wazipay.data.network.models.common.LoginRequestBody
import com.escrow.wazipay.data.network.models.common.LoginResponseBody
import com.escrow.wazipay.data.network.models.common.RegistrationRequestBody
import com.escrow.wazipay.data.network.models.common.RegistrationResponseBody
import com.escrow.wazipay.data.network.models.common.SetPinRequestBody
import com.escrow.wazipay.data.network.models.common.SetPinResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @POST("auth/register")
    suspend fun registerUser(
        @Body registrationRequestBody: RegistrationRequestBody
    ): Response<RegistrationResponseBody>

    @PUT("auth/pin")
    suspend fun login(
        @Body loginRequestBody: LoginRequestBody
    ): Response<LoginResponseBody>

    @POST("auth/login")
    suspend fun setUserPin(
        @Body setPinRequestBody: SetPinRequestBody
    ): Response<SetPinResponseBody>
}