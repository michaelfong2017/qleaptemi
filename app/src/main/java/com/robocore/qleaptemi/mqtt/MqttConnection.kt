package com.robocore.qleaptemi.mqtt

import android.content.Context
import android.util.Log
import com.robocore.qleaptemi.inject.MqttCoroutineScope
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

/** Remember to add
 * # Automatically convert third-party libraries to use AndroidX
 * android.enableJetifier=true
 * to gradle.properties
 *
 * Remember to add
 * <service android:name="org.eclipse.paho.android.service.MqttService" />
 * to AndroidManifest.xml under application
 */
class MqttConnection(private val context: Context) {
    /** Needed for field injection for a custom class
     * e.g. hiltEntryPoint.externalScope() provides the desired externalScope.
     */
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface MqttConnectionEntryPoint {
        @MqttCoroutineScope
        fun externalScope(): CoroutineScope
    }

    private val hiltEntryPoint =
        EntryPointAccessors.fromApplication(context, MqttConnectionEntryPoint::class.java)
    /** Needed for field injection for a custom class END */

    /** The clientId of the client associated with this `Connection` object  */
    private var clientId: String? = null

    /** [ConnectionStatus] of the [MqttAndroidClient] represented by this `Connection` object. Default value is [ConnectionStatus.NONE]  */
    private val _status: MutableStateFlow<ConnectionStatus> = MutableStateFlow(
        ConnectionStatus.NONE
    )
    val status = _status.asStateFlow()
    fun setStatus(status: ConnectionStatus) {
        _status.value = status
    }
    /***/

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

    init {
        mqttConnectOptions = MqttConnectOptions().also {
            it.isAutomaticReconnect = true
            it.userName = username
            it.password = password.toCharArray()
        }

        if (client == null) {
            clientId = MqttClient.generateClientId()
            client = MqttAndroidClient(context, broker_url, clientId)
            client?.setCallback(MqttCallbackHandler())
        }
    }

    /**
     * Determines if the client is connected
     * @return is the client connected
     */
    fun isConnected(): Boolean {
        Log.d(TAG, context.toString())
        return status.value == ConnectionStatus.CONNECTED
    }

    fun connect() {
        val externalScope = hiltEntryPoint.externalScope()
        Log.d(TAG, externalScope.toString())

        externalScope.launch {
            try {
                if (client?.isConnected == false) {
                    Log.d(TAG, "MqttClient - connect - mqttClient?.isConnected == false")
                    client?.connect(mqttConnectOptions, null, object : IMqttActionListener {
                        override fun onSuccess(asyncActionToken: IMqttToken?) {
                            Log.d(TAG, "MqttClient - Connected")

                            setStatus(ConnectionStatus.CONNECTED)
                        }

                        override fun onFailure(
                            asyncActionToken: IMqttToken?,
                            exception: Throwable?
                        ) {
                            Log.d(TAG, "MqttClient - Failed to connect - $exception")

                            setStatus(ConnectionStatus.ERROR)
                        }
                    })
                }
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }   // end coroutine
    }

    companion object {
        private const val TAG = "MqttConnection"
        private const val broker_url = "ssl://service.robocore.ai:8883"
        private const val username = "robocore"
        private const val password = "temi"
    }
}