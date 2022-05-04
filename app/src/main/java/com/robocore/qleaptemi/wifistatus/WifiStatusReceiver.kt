package com.robocore.qleaptemi.wifistatus

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.util.Log
import com.robocore.qleaptemi.mqtt.MqttConnection
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class WifiConnectionStatus {
    /** Client is Connecting  */
    CONNECTING,

    /** Client is Connected  */
    CONNECTED,

    /** Client is Disconnecting  */
    DISCONNECTING,

    /** Client is Disconnected  */
    DISCONNECTED,

    /** Client has encountered an Error  */
    ERROR,

    /** Status is unknown  */
    NONE
}

class WifiStatusReceiver(context: Context) : BroadcastReceiver() {
    /** Needed for field injection for a custom class
     * e.g. hiltEntryPoint.externalScope() provides the desired externalScope.
     */
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WifiStatusReceiverEntryPoint {
        fun mqttConnection(): MqttConnection
    }

    private val hiltEntryPoint =
        EntryPointAccessors.fromApplication(context, WifiStatusReceiverEntryPoint::class.java)

    /** Needed for field injection for a custom class END */

    private val _status: MutableStateFlow<WifiConnectionStatus> = MutableStateFlow(
        WifiConnectionStatus.NONE
    )
    val status = _status.asStateFlow()
    private fun setStatus(status: WifiConnectionStatus) {
        _status.value = status
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        val action = p1?.action
        if (action == WifiManager.NETWORK_STATE_CHANGED_ACTION) {
            val info: NetworkInfo? = p1.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO)
            val connected = info?.isConnected

            Log.d("WifiStatusReceiver", "connected: $connected")

            if (connected == true) {
                setStatus(WifiConnectionStatus.CONNECTED)

                val mqttConnection = hiltEntryPoint.mqttConnection()
                CoroutineScope(Dispatchers.Default).launch {
                    while (!mqttConnection.isConnected()) {
                        mqttConnection.reconnect()
                        delay(100)
                    }
                }

            } else {
                setStatus(WifiConnectionStatus.DISCONNECTED)
            }
        }
    }
}
