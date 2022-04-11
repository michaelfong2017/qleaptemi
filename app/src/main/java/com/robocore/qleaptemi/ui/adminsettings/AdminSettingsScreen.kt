package com.robocore.qleaptemi.ui.adminsettings

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.robocore.qleaptemi.Screen

@Composable
fun AdminSettingsScreen(navController: NavController) {
    Button(onClick = { navController.navigate(Screen.Home.route) }) {
        Text(text = "adminsettings")
    }
}