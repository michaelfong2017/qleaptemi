package com.robocore.qleaptemi.ui.adminsettings

import android.util.Log
import androidx.lifecycle.ViewModel
import com.robocore.qleaptemi.mqtt.MqttConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminSettingsViewModel @Inject constructor(): ViewModel() {
    @Inject lateinit var mqttConnection: MqttConnection
    fun test() {
        Log.d(TAG, mqttConnection.toString())
    }

    companion object {
        private const val TAG = "AdminSettingsViewModel"
    }
}