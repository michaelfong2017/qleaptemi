package com.robocore.qleaptemi.ui.home

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.robocore.qleaptemi.Screen

@Composable
fun HomeScreen(navController: NavController) {
    Button(onClick = { navController.navigate(Screen.Settings.route) }) {
        Text(text = "home")
    }
}
