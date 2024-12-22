package com.escrow.wazipay

import android.app.Application
import com.escrow.wazipay.container.AppContainer
import com.escrow.wazipay.container.AppContainerImpl

class Wazipay: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}