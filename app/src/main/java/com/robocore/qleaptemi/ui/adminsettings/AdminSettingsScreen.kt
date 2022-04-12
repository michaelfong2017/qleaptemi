package com.robocore.qleaptemi.ui.adminsettings

import android.util.Log
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.robocore.qleaptemi.ui.home.HomeViewModel
import com.robocore.qleaptemi.MainScreen

@Composable
fun AdminSettingsScreen(navController: NavController, viewModel: AdminSettingsViewModel) {
    Log.d("ViewModelObject", "AdminSettingsScreen: $viewModel")
    viewModel.test()

    Button(onClick = {
        navController.navigate(MainScreen.Home.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
        }
    }) {
        Text(text = "adminsettings")
    }
}