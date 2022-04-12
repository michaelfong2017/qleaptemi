package com.robocore.qleaptemi.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robocore.qleaptemi.mqtt.MqttConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {
    @Inject lateinit var mqttConnection: MqttConnection

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    fun test() {
        Log.d(TAG, mqttConnection.toString())
    }

    fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.StartOrStopQLeap -> {
//                coroutineScope.launch {
//                    _viewState.value = _viewState.value.copy(isLoading = true)
//                    withContext(Dispatchers.IO) { answerService.save(uiAction.answer) }
//                    val text = if (uiAction.answer == "Nacho cheese") {
//                        "You've heard too many cheese jokes"
//                    } else {
//                        "Nacho cheese"
//                    }
//                    _viewState.value = _viewState.value.copy(textToDisplay = text)
//                    _oneShotEvents.send(OneShotEvent.NavigateToResults)
//                    _viewState.value = _viewState.value.copy(isLoading = false)
//                }
                viewModelScope.launch {
                    mqttConnection.connect()
                    Log.d(TAG, mqttConnection.getStatus().toString())
                }
            }
            is UiAction.OpenOrCloseSettings -> {

            }
            is UiAction.OpenOrCloseAdminSettings -> {

            }
        }
    }

    data class ViewState(
        val mqttConnectionStatus: MqttConnection.ConnectionStatus = MqttConnection.ConnectionStatus.NONE,
    )

//    sealed class OneShotEvent {
//        object NavigateToResults : OneShotEvent()
//    }

    sealed class UiAction {
        object StartOrStopQLeap : UiAction()
        object OpenOrCloseSettings : UiAction()
        object OpenOrCloseAdminSettings : UiAction()
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}