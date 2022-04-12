package com.robocore.qleaptemi.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.robocore.qleaptemi.mqtt.MqttConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {
    @Inject lateinit var mqttConnection: MqttConnection
    fun test() {
        Log.d(TAG, mqttConnection.toString())
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val textToDisplay: String = "",
    )

    sealed class OneShotEvent {
        object NavigateToResults : OneShotEvent()
    }

    sealed class UiAction {
        class AnswerConfirmed(val answer: String) : UiAction()
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}