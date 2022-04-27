package com.robocore.qleaptemi.wifistatus

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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

class WifiStatusReceiver() : BroadcastReceiver() {
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
            }
            else {
                setStatus(WifiConnectionStatus.DISCONNECTED)
            }
        }
    }
}
