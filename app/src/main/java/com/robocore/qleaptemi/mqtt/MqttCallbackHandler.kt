package com.robocore.qleaptemi.mqtt

import android.content.Context
import android.util.Log
import com.robocore.qleaptemi.inject.MqttCoroutineScope
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class MqttCallbackHandler(context: Context) : MqttCallback {
    /** Needed for field injection for a custom class
     * e.g. hiltEntryPoint.externalScope() provides the desired externalScope.
     */
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface MqttCallbackHandlerEntryPoint {
        fun mqttConnection(): MqttConnection
    }

    private val hiltEntryPoint =
        EntryPointAccessors.fromApplication(context, MqttCallbackHandlerEntryPoint::class.java)
    /** Needed for field injection for a custom class END */

    override fun connectionLost(cause: Throwable?) {
        val mqttConnection = hiltEntryPoint.mqttConnection()
        mqttConnection.setStatus(MqttConnection.ConnectionStatus.DISCONNECTED)

    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        Log.d(TAG, "messageArrived: ${message.toString()} from topic: $topic")

    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {

    }

    companion object {
        private const val TAG = "MqttCallbackHandler"
    }
}