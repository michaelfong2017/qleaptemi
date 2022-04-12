package com.robocore.qleaptemi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.robocore.qleaptemi.ui.adminsettings.AdminSettingsScreen
import com.robocore.qleaptemi.ui.adminsettings.AdminSettingsViewModel
import com.robocore.qleaptemi.ui.home.HomeScreen
import com.robocore.qleaptemi.ui.home.HomeViewModel
import com.robocore.qleaptemi.ui.settings.SettingsScreen
import com.robocore.qleaptemi.ui.settings.SettingsViewModel

internal sealed class MainScreen(val route: String) {
    object Home : MainScreen("home")
    object Settings : MainScreen("settings")
    object AdminSettings : MainScreen("adminsettings")
}

@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    /** HiltViewModels are provided here so that they are scoped to the navigation graph */
    /** If HiltViewModels were provided inside the individual composables instead, they
     * would be scoped to the navigation route. Any new route (not navigating back) to the
     * same composable would then provides another instance of HiltViewModel. */
    val homeViewModel: HomeViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val adminViewModel: AdminSettingsViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = MainScreen.Home.route,
        modifier = modifier,
    ) {
        composable(MainScreen.Home.route) {
            HomeScreen(navController, homeViewModel)
        }
        composable(MainScreen.Settings.route) {
            SettingsScreen(navController, settingsViewModel)
        }
        composable(MainScreen.AdminSettings.route) {
            AdminSettingsScreen(navController, adminViewModel)
        }
    }
}
