package com.escrow.wazipay.container

import com.escrow.wazipay.data.network.repository.ApiRepository
import com.escrow.wazipay.data.room.repository.DBRepository

interface AppContainer {
    val apiRepository: ApiRepository
    val dbRepository: DBRepository
}