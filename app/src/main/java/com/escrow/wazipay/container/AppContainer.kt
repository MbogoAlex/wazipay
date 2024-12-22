package com.escrow.wazipay.container

import com.escrow.wazipay.data.network.repository.ApiRepository

interface AppContainer {
    val apiRepository: ApiRepository
}