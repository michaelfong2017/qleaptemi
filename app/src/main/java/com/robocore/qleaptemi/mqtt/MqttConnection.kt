package com.robocore.qleaptemi.mqtt

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions

class MqttConnection(private val context: Context) {
    /** The clientId of the client associated with this `Connection` object  */
    private val clientId: String? = null

    /** [ConnectionStatus] of the [MqttAndroidClient] represented by this `Connection` object. Default value is [ConnectionStatus.NONE]  */
    private var status = ConnectionStatus.NONE

    /** The [MqttAndroidClient] instance this class represents  */
    private var client: MqttAndroidClient? = null

    /** The [MqttConnectOptions] that were used to connect this client  */
    private var mqttConnectOptions: MqttConnectOptions? = null

    /** True if this connection is secured using TLS  */
    private var tlsConnection = true

    /** The list of this connection's subscriptions  */
    private var subscriptions: Map<String, String> = HashMap<String, String>()

    enum class ConnectionStatus {
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

    /**
     * Determines if the client is connected
     * @return is the client connected
     */
    fun isConnected(): Boolean {
        Log.d(TAG, context.toString())
        return status == ConnectionStatus.CONNECTED
    }

    companion object {
        private const val TAG = "MqttConnection"
        private const val broker_url = "ssl://service.robocore.ai:8883"
        private const val username = "robocore"
        private const val password = "temi"
    }
}