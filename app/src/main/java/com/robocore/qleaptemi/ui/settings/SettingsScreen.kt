package com.robocore.qleaptemi.ui.settings

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.robocore.qleaptemi.MainScreen
import com.robocore.qleaptemi.settings.VolumePreference
import com.robocore.qleaptemi.ui.theme.QLeapTemiTheme

@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel) {
    Log.d("ViewModelObject", "SettingsScreen: $viewModel")

    val uiState = viewModel.uiState.collectAsState()

    Button(onClick = {
        navController.navigate(MainScreen.AdminSettings.route) {
            popUpTo(navController.graph.findStartDestination().id) {

            }
        }
    }) {
        Text(text = "settings")
    }

    _SettingsScreen(
        initPreferences = {},
        onVolumeSelected = viewModel::onVolumeSelected,
        currentVolume = uiState.value.volume,
    )
}

@Composable
fun _SettingsScreen(
    initPreferences: () -> Unit = {},
    onVolumeSelected: (selected: Int) -> Unit = {},
    currentVolume: Int = 0,
) {
    LaunchedEffect(true) {
        initPreferences()
    }

    Column(
        Modifier
            .background(Color.White)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxHeight()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                item {
                    Text(
                        text = "Temi speak volume:",
                        fontSize = 30.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                item {
                    VolumePreference(
                        onVolumeSelected,
                        currentVolume
                    )
                }
            }

            Spacer(modifier = Modifier.fillMaxWidth(0.2f))
        }

    }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
fun DefaultPreview() {
    QLeapTemiTheme(darkTheme = true) {
        Box {
            _SettingsScreen()
        }
    }
}
