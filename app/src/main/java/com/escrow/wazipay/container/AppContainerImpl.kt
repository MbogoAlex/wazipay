package com.escrow.wazipay.container

import android.content.Context
import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.network.repository.ApiRepositoryImpl
import com.escrow.wazipay.data.network.repository.ApiService
import com.escrow.wazipay.data.room.AppDatabase
import com.escrow.wazipay.data.room.repository.DBRepository
import com.escrow.wazipay.data.room.repository.DBRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class AppContainerImpl(context: Context): AppContainer {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    private val baseUrl = "http://172.105.90.112:8080/wazipay-api/api/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    override val apiRepository: ApiRepository by lazy {
        ApiRepositoryImpl(retrofitService)
    }

    override val dbRepository: DBRepository by lazy {
        DBRepositoryImpl(AppDatabase.getDatabase(context).appDao())
    }
}