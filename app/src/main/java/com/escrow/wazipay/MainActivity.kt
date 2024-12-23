package com.escrow.wazipay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.escrow.wazipay.ui.nav.NavigationGraph
import com.escrow.wazipay.ui.theme.WazipayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WazipayTheme {
                NavigationGraph(navController = rememberNavController())
            }
        }
    }
}
