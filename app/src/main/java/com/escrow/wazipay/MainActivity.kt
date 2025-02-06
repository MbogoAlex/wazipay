package com.escrow.wazipay

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.escrow.wazipay.ui.nav.NavigationGraph
import com.escrow.wazipay.ui.theme.WazipayTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainActivityViewModel = viewModel(factory = AppViewModelFactory.Factory)
            val uiState by viewModel.uiState.collectAsState()
            WazipayTheme(
                darkTheme = uiState.darkMode.darkMode
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    NavigationGraph(
                        navController = rememberNavController(),
                        darkMode = uiState.darkMode.darkMode,
                        onSwitchTheme = {
                            viewModel.switchTheme()
                        }
                    )
                }
            }
        }
    }
}
