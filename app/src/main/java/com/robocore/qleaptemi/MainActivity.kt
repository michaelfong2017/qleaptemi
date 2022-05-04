package com.robocore.qleaptemi

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.robocore.qleaptemi.audio.VolumeReceiver
import com.robocore.qleaptemi.mqtt.MqttConnection
import com.robocore.qleaptemi.ui.theme.QLeapTemiTheme
import com.robocore.qleaptemi.wifistatus.WifiStatusReceiver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var broadcastReceiver: WifiStatusReceiver
    @Inject
    lateinit var volumeReceiver: VolumeReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QLeapTemiTheme(darkTheme = true) {
                // A surface container using the 'background' color from the theme
                QLeapApp()
            }
        }

        /** Request permissions */
        if (allPermissionsGranted()) {
            /***********************/
            /** Permission granted */
            /***********************/
            onAllPermissionsGranted()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    /** Request permissions */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun onAllPermissionsGranted() {
        Log.d("MainActivity", "onAllPermissionsGranted")

        /** Init and register broadcast receivers */
        val wifiStatusIntentFilter = IntentFilter()
        wifiStatusIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        registerReceiver(broadcastReceiver, wifiStatusIntentFilter)
        val volumeIntentFilter = IntentFilter()
        volumeIntentFilter.addAction("android.media.VOLUME_CHANGED_ACTION")
        registerReceiver(volumeReceiver, volumeIntentFilter)
        /** Init and register broadcast receivers END */
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                /***********************/
                /** Permission granted */
                /***********************/
                onAllPermissionsGranted()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)

    }
}

@Composable
fun QLeapApp() {
    val navController = rememberNavController()

//    Scaffold(
//
//    ) { innerPadding ->
        AppNavigation(
            navController = navController,
//            modifier = Modifier.padding(innerPadding),
            modifier = Modifier,
        )
//    }
}
