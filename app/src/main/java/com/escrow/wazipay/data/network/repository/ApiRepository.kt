package com.escrow.wazipay.data.network.repository

import com.escrow.wazipay.data.network.models.common.LoginRequestBody
import com.escrow.wazipay.data.network.models.common.LoginResponseBody
import com.escrow.wazipay.data.network.models.common.RegistrationRequestBody
import com.escrow.wazipay.data.network.models.common.RegistrationResponseBody
import com.escrow.wazipay.data.network.models.common.SetPinRequestBody
import com.escrow.wazipay.data.network.models.common.SetPinResponseBody
import retrofit2.Response

interface ApiRepository {
    suspend fun registerUser(
       registrationRequestBody: RegistrationRequestBody
    ): Response<RegistrationResponseBody>

    suspend fun login(
        loginRequestBody: LoginRequestBody
    ): Response<LoginResponseBody>

    suspend fun setUserPin(
       setPinRequestBody: SetPinRequestBody
    ): Response<SetPinResponseBody>
}