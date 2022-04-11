package com.robocore.qleaptemi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.robocore.qleaptemi.ui.adminsettings.AdminSettingsScreen
import com.robocore.qleaptemi.ui.home.HomeScreen
import com.robocore.qleaptemi.ui.settings.SettingsScreen

internal sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Settings : Screen("settings")
    object AdminSettings : Screen("adminsettings")
}

@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
        composable(Screen.AdminSettings.route) {
            AdminSettingsScreen(navController)
        }
    }
}
