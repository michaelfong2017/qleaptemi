package com.robocore.qleaptemi.mqtt

import android.util.Log
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class MqttCallbackHandler : MqttCallback {
    override fun connectionLost(cause: Throwable?) {

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