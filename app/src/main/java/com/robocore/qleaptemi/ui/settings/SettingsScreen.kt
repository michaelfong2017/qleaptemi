package com.robocore.qleaptemi.ui.settings

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.robocore.qleaptemi.Screen

@Composable
fun SettingsScreen(navController: NavController) {
    Button(onClick = { navController.navigate(Screen.AdminSettings.route) }) {
        Text(text = "settings")
    }
}
