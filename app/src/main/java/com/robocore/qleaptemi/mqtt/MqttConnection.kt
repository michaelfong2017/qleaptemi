package com.robocore.qleaptemi.mqtt

import android.content.Context
import android.util.Log
import com.robocore.qleaptemi.inject.MqttCoroutineScope
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
            client?.setCallback(MqttCallbackHandler(context = context))
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
                Log.d("MqttClient", "connect - status.value: ${status.value}")
                Log.d("MqttClient", "connect - isConnected: ${client?.isConnected}")
                when (status.value) {
                    ConnectionStatus.CONNECTING -> {}
                    ConnectionStatus.CONNECTED -> {}
                    ConnectionStatus.DISCONNECTING -> {}
                    ConnectionStatus.DISCONNECTED, ConnectionStatus.ERROR, ConnectionStatus.NONE -> {
                        client?.connect(mqttConnectOptions, null, object : IMqttActionListener {
                            override fun onSuccess(asyncActionToken: IMqttToken?) {
                                Log.d(TAG, "MqttClient - Connected")

                                setStatus(ConnectionStatus.CONNECTED)
                            }

                            override fun onFailure(
                                asyncActionToken: IMqttToken?,
                                exception: Throwable?
                            ) {
                                Log.e(TAG, "MqttClient - Failed to connect - $exception")

                                setStatus(ConnectionStatus.ERROR)
                            }
                        })

                        setStatus(ConnectionStatus.CONNECTING)
                    }
                }
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }   // end coroutine
    }

    fun disconnect() {
        val externalScope = hiltEntryPoint.externalScope()

        externalScope.launch {
            try {
                Log.d("MqttClient", "disconnect - status.value: ${status.value}")
                Log.d("MqttClient", "disconnect - isConnected: ${client?.isConnected}")
                when (status.value) {
                    ConnectionStatus.CONNECTING -> {}
                    ConnectionStatus.DISCONNECTING -> {}
                    ConnectionStatus.DISCONNECTED -> {}
                    ConnectionStatus.CONNECTED, ConnectionStatus.ERROR, ConnectionStatus.NONE -> {
                        if (client?.isConnected == true) {
                            client?.disconnect(null, object : IMqttActionListener {
                                override fun onSuccess(asyncActionToken: IMqttToken?) {
                                    Log.d(TAG, "MqttClient - Disconnected")
                                    setStatus(ConnectionStatus.DISCONNECTED)
                                }

                                override fun onFailure(
                                    asyncActionToken: IMqttToken?,
                                    exception: Throwable?
                                ) {
                                    Log.e(TAG, "MqttClient - Failed to disconnect")
                                    setStatus(ConnectionStatus.ERROR)
                                }
                            })

                            setStatus(ConnectionStatus.DISCONNECTING)
                        }
                    }
                }
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }
    }

    fun reconnect() {
        val externalScope = hiltEntryPoint.externalScope()

        externalScope.launch {
            try {
                Log.d("MqttClient", "reconnect - status.value: ${status.value}")
                Log.d("MqttClient", "reconnect - isConnected: ${client?.isConnected}")
                when (status.value) {
                    ConnectionStatus.CONNECTING -> {}
                    ConnectionStatus.CONNECTED -> {}
                    ConnectionStatus.DISCONNECTING -> {}
                    /** Even if connectionLost was called (client should be disconnected),
                     * mqtt client may not be connected (e.g. connectionLost due to wifi disconnected)
                     * since "Client is connected (32100)" will be thrown. */
                    ConnectionStatus.DISCONNECTED -> {
                        if (client?.isConnected == true) {
                            client?.disconnect(null, object : IMqttActionListener {
                                override fun onSuccess(asyncActionToken: IMqttToken?) {
                                    Log.d(TAG, "MqttClient - Disconnected")
                                    setStatus(ConnectionStatus.DISCONNECTED)
                                    connect()
                                }

                                override fun onFailure(
                                    asyncActionToken: IMqttToken?,
                                    exception: Throwable?
                                ) {
                                    Log.e(TAG, "MqttClient - Failed to disconnect")
                                    setStatus(ConnectionStatus.ERROR)
                                }
                            })

                            setStatus(ConnectionStatus.DISCONNECTING)
                        }
                        else {
                            connect()
                        }
                    }
                    ConnectionStatus.ERROR, ConnectionStatus.NONE -> {
                        if (client?.isConnected == true) {
                            client?.disconnect(null, object : IMqttActionListener {
                                override fun onSuccess(asyncActionToken: IMqttToken?) {
                                    Log.d(TAG, "MqttClient - Disconnected")
                                    setStatus(ConnectionStatus.DISCONNECTED)
                                    connect()
                                }

                                override fun onFailure(
                                    asyncActionToken: IMqttToken?,
                                    exception: Throwable?
                                ) {
                                    Log.e(TAG, "MqttClient - Failed to disconnect")
                                    setStatus(ConnectionStatus.ERROR)
                                }
                            })

                            setStatus(ConnectionStatus.DISCONNECTING)
                        }
                        else {
                            connect()
                        }
                    }
                }
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val TAG = "MqttConnection"
        private const val broker_url = "ssl://service.robocore.ai:8883"
        private const val username = "robocore"
        private const val password = "temi"
    }
}