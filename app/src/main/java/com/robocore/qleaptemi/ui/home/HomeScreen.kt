package com.robocore.qleaptemi.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.robocore.qleaptemi.MainScreen
import com.robocore.qleaptemi.mqtt.MqttConnection
import com.robocore.qleaptemi.ui.theme.QLeapTemiTheme

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    Log.d("ViewModelObject", "HomeScreen: $viewModel")

    val viewState = viewModel.viewState.collectAsState()

    _HomeScreen(
        viewState.value,
        {
            navController.navigate(MainScreen.Settings.route) {
                popUpTo(navController.graph.findStartDestination().id) {

                }
            }
        },
        { viewModel.onAction(HomeViewModel.UiAction.StartOrStopQLeap) }
    )
}

@Composable
fun _HomeScreen(
    viewState: HomeViewModel.ViewState,
    navigateTo: () -> Unit,
    onActionStartOrStopQLeap: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
    ) {
        Spacer(modifier = Modifier.fillMaxWidth(0.03f))
        Column(
            modifier = Modifier.fillMaxWidth(0.25f)
        ) {
//            Spacer(modifier = Modifier.scale(1f, 0.75f))

            Spacer(modifier = Modifier.fillMaxHeight(0.73f))
            Text(
                text = "Temi狀態",
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                color = Color.White,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(4.dp)
            )
            Text(
                when (viewState.mqttConnectionStatus) {
                    MqttConnection.ConnectionStatus.CONNECTED -> "伺服器連接: 已連接"
                    MqttConnection.ConnectionStatus.DISCONNECTED -> "伺服器連接: 已斷開"
                    MqttConnection.ConnectionStatus.CONNECTING -> "CONNECTING"
                    MqttConnection.ConnectionStatus.DISCONNECTING -> "DISCONNECTING"
                    MqttConnection.ConnectionStatus.ERROR -> "ERROR"
                    MqttConnection.ConnectionStatus.NONE -> "NONE"
                },
                fontSize = 24.sp,
                color = Color.White,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(4.dp)
            )

            Button(onClick = navigateTo) {
                Text(text = "home")
            }

            Button(onClick = onActionStartOrStopQLeap ) {
                Text(text = "connect mqtt")
            }

        }
    }


//            Text(
//                if (uiState.wifiStatus) "WiFi連接: 已連接" else "WiFi連接: 已斷開",
//                fontSize = 24.sp,
//                color = Color.White,
//                textAlign = TextAlign.End,
//                modifier = Modifier.padding(4.dp)
//            )
//            Text(
//                text = "音量: ${uiState.volume}",
//                fontSize = 24.sp,
//                color = Color.White,
//                textAlign = TextAlign.End,
//                modifier = Modifier.padding(4.dp)
//            )
//
//        }
//
//        Column(
//            modifier = Modifier.fillMaxWidth(0.64f),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            if (uiState.passwordInputDialogIsVisible) {
//
//            } else {
//                Spacer(modifier = Modifier.fillMaxHeight(0.18f))
//
//                OutlinedButton(
//                    onClick = { qLeapEventHandler.finishAllOccurrencesOfCurrentTask() },
//                    shape = CircleShape,
//                    colors = ButtonDefaults.outlinedButtonColors(backgroundColor = if (uiState.isRunning) Red else LightGrey),
//                    modifier = Modifier
//                        .alpha(1f)
//                        .size(300.dp, 300.dp)
//                ) {
//                    Text(
//                        text = "停止",
//                        color = Color.White,
//                        fontSize = 72.sp,
//                    )
//                }
//                Spacer(modifier = Modifier.size(height = 70.dp, width = 0.dp))
//                Text(
//                    text = if (uiState.isRunning) "程式啟動中" else "程式已關閉",
//                    fontSize = 36.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White,
//                )
//            }
//        }
//
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.End,
//            verticalArrangement = Arrangement.Bottom,
////            modifier = Modifier.padding(end = 20.dp, bottom = 700.dp)
//        ) {
//            // setting button
//            OutlinedButton(
//                onClick = {
//                    // input password
//                    //TODO: set last action, show password input dialog
//                    viewModel.updateLastAction(Action.SETTING)
//                    viewModel.showPasswordInputDialog(true)
//                },
//                modifier = Modifier
//                    .size(100.dp, 100.dp),
//                colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
//                border = BorderStroke(0.dp, Grey)
//            ) {
//                Image(
//                    painterResource(R.drawable.settings),
//                    contentDescription = "settings",
//                    modifier = Modifier
//                        .fillMaxSize()
//                )
//            }
//
//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier
//                    .fillMaxHeight(0.70f)
//                    .fillMaxWidth()
//            ) {
//                OutlinedButton(
//                    onClick = {
//                        Robot.getInstance().goTo("home base")
//                    },
//                    modifier = Modifier
//                        .size(200.dp, 200.dp),
//                    colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Grey),
//                    border = BorderStroke(0.dp, Color.Transparent),
//                    shape = CircleShape,
//                ) {
//                    Image(
//                        painterResource(R.drawable.charging_base),
//                        contentDescription = "charging_base",
//                        modifier = Modifier
//                            .fillMaxSize(),
//                    )
//                }
//            }
//
////            Spacer(modifier = Modifier.fillMaxHeight(0.71f))
//            // goToHomeBaseButton
//
//            Column(
//                modifier = Modifier.fillMaxSize(),
//                horizontalAlignment = Alignment.Start,
//                verticalArrangement = Arrangement.Top,
//            ) {
//                Text(
//                    text = "工作範圍",
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 36.sp,
//                    color = Color.White,
//                    textAlign = TextAlign.Start,
//                    modifier = Modifier.padding(4.dp)
//                )
//                Text(
//                    text = "床位: $bedroom",
//                    fontSize = 24.sp,
//                    color = Color.White,
//                    textAlign = TextAlign.Start,
//                    modifier = Modifier.padding(4.dp)
//                )
//                Text(
//                    text = "地點: ${if (mqttTopicSubscribe) siteId else "N/A"}",
//                    fontSize = 24.sp,
//                    color = Color.White,
//                    textAlign = TextAlign.Start,
//                    modifier = Modifier.padding(4.dp)
//                )
//            }
//
//        }
//
////        Spacer(modifier = Modifier.fillMaxSize(0.75f))
//
//    }
//
//    Text(
//        text = uiState.banner,
//        fontWeight = FontWeight.Bold,
//        fontSize = 48.sp,
//        color = Color.White,
//        textAlign = TextAlign.Center,
//        modifier = Modifier.padding(30.dp)
//    )

}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
fun DefaultPreview() {
    QLeapTemiTheme(darkTheme = true) {
        Box {
            _HomeScreen(HomeViewModel.ViewState(), {}, {})
        }
    }
}
