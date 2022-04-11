package com.robocore.qleaptemi

import android.util.Log
import androidx.lifecycle.ViewModel
import com.robocore.qleaptemi.mqtt.MqttConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {
    @Inject lateinit var mqttConnection: MqttConnection
    fun test() {
        Log.d("MainViewModel", "yo")
        Log.d("MainViewModel", mqttConnection.toString())
    }
}