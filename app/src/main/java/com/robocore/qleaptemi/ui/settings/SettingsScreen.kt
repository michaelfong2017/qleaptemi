package com.robocore.qleaptemi.ui.settings

import android.util.Log
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.robocore.qleaptemi.MainScreen
import com.robocore.qleaptemi.ui.home.HomeViewModel

@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel) {
    Log.d("ViewModelObject", "SettingsScreen: $viewModel")
    viewModel.test()

    Button(onClick = {
        navController.navigate(MainScreen.AdminSettings.route) {
            popUpTo(navController.graph.findStartDestination().id) {

            }
        }
    }) {
        Text(text = "settings")
    }
}
