package com.robocore.qleaptemi.ui.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.robocore.qleaptemi.MainScreen
import com.robocore.qleaptemi.R
import com.robocore.qleaptemi.mqtt.MqttConnection
import com.robocore.qleaptemi.ui.theme.Grey
import com.robocore.qleaptemi.ui.theme.LightGrey
import com.robocore.qleaptemi.ui.theme.QLeapTemiTheme
import com.robocore.qleaptemi.wifistatus.WifiConnectionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    Log.d("ViewModelObject", "HomeScreen: $viewModel")

    val uiState = viewModel.uiState.collectAsState()

    _HomeScreen(
        navigateTo = {
            navController.navigate(MainScreen.Settings.route) {
                popUpTo(navController.graph.findStartDestination().id) {

                }
            }
        },
        uiState = uiState.value,
        setEvent = { viewModel.setEvent(HomeViewModel.Event.ConnectMqttTest) },
        effect = viewModel.effect,
        setEffect = { viewModel.setEffect { HomeViewModel.Effect.ShowToastTest } },
    )
}

@Composable
fun _HomeScreen(
    navigateTo: () -> Unit = {},
    uiState: HomeViewModel.State = HomeViewModel.State(),
    setEvent: (HomeViewModel.Event) -> Unit = {},
    effect: Flow<HomeViewModel.Effect> = emptyFlow(),
    setEffect: (HomeViewModel.Effect) -> Unit = {},
) {
    val context = LocalContext.current

    LaunchedEffect({}) {
        effect.collect {
            Log.d("HomeScreen", "effect.collect($it)")
            when (it) {
                HomeViewModel.Effect.ShowToastTest -> {
                    Toast.makeText(context, "Test", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
    ) {
        Spacer(modifier = Modifier.fillMaxWidth(0.03f))
        Column(
            modifier = Modifier.fillMaxWidth(0.25f)
        ) {
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
                when (uiState.mqttConnectionStatus) {
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

//            Button(onClick = navigateTo) {
//                Text(text = "home")
//            }
//
//            Button(onClick = {
//                setEvent(HomeViewModel.Event.ConnectMqttTest)
//                setEffect(HomeViewModel.Effect.ShowToastTest)
//            }) {
//                Text(text = "connect/disconnect/reconnect mqtt test")
//            }


            Text(
                when (uiState.wifiStatus) {
                    WifiConnectionStatus.CONNECTED -> "WiFi連接: 已連接"
                    WifiConnectionStatus.DISCONNECTED -> "WiFi連接: 已斷開"
                    WifiConnectionStatus.CONNECTING -> "CONNECTING"
                    WifiConnectionStatus.DISCONNECTING -> "DISCONNECTING"
                    WifiConnectionStatus.ERROR -> "ERROR"
                    WifiConnectionStatus.NONE -> "NONE"
                },
                fontSize = 24.sp,
                color = Color.White,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "音量: ${uiState.volume}",
                fontSize = 24.sp,
                color = Color.White,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(4.dp)
            )

        }

        Column(
            modifier = Modifier.fillMaxWidth(0.64f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.18f))

            OutlinedButton(
                onClick = { },
                shape = CircleShape,
                colors = ButtonDefaults.outlinedButtonColors(backgroundColor = if (true) Red else LightGrey),
                modifier = Modifier
                    .alpha(1f)
                    .size(300.dp, 300.dp)
            ) {
                Text(
                    text = "停止",
                    color = Color.White,
                    fontSize = 72.sp,
                )
            }
            Spacer(modifier = Modifier.size(height = 70.dp, width = 0.dp))
            Text(
                text = if (true) "程式啟動中" else "程式已關閉",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom,
//            modifier = Modifier.padding(end = 20.dp, bottom = 700.dp)
        ) {
            // setting button
            OutlinedButton(
                onClick = navigateTo,
                modifier = Modifier
                    .size(100.dp, 100.dp),
                colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
                border = BorderStroke(0.dp, Grey)
            ) {
                Image(
                    painterResource(R.drawable.settings),
                    contentDescription = "settings",
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxHeight(0.70f)
                    .fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = {
//                        Robot.getInstance().goTo("home base")
                    },
                    modifier = Modifier
                        .size(200.dp, 200.dp),
                    colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Grey),
                    border = BorderStroke(0.dp, Color.Transparent),
                    shape = CircleShape,
                ) {
                    Image(
                        painterResource(R.drawable.charging_base),
                        contentDescription = "charging_base",
                        modifier = Modifier
                            .fillMaxSize(),
                    )
                }
            }

//            Spacer(modifier = Modifier.fillMaxHeight(0.71f))
            // goToHomeBaseButton

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
            ) {
                Text(
                    text = "工作範圍",
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(4.dp)
                )
                Text(
                    text = "床位: bedroom",
                    fontSize = 24.sp,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(4.dp)
                )
                Text(
                    text = "地點: ${if (true) "siteId" else "N/A"}",
                    fontSize = 24.sp,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(4.dp)
                )
            }

        }

//        Spacer(modifier = Modifier.fillMaxSize(0.75f))

    }

    Text(
        text = "banner",
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        color = Color.White,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(30.dp)
    )

}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
fun DefaultPreview() {
    QLeapTemiTheme(darkTheme = true) {
        Box {
            _HomeScreen()
        }
    }
}
