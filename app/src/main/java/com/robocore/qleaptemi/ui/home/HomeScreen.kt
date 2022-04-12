package com.robocore.qleaptemi.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.robocore.qleaptemi.MainScreen
import com.robocore.qleaptemi.mqtt.MqttConnection
import com.robocore.qleaptemi.ui.theme.QLeapTemiTheme
import javax.inject.Inject

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    Log.d("ViewModelObject", "HomeScreen: $viewModel")
    viewModel.test()

    _HomeScreen {
        navController.navigate(MainScreen.Settings.route) {
            popUpTo(navController.graph.findStartDestination().id) {

            }
        }
    }
}

@Composable
fun _HomeScreen(navigateTo: () -> Unit) {
    Button(onClick = navigateTo) {
        Text(text = "home")
    }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
fun DefaultPreview() {
    QLeapTemiTheme {
        Box {
            _HomeScreen {}
        }
    }
}
