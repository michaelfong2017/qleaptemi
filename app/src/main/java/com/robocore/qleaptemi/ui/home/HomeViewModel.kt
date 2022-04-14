package com.robocore.qleaptemi.ui.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.robocore.qleaptemi.BaseViewModel
import com.robocore.qleaptemi.UiEffect
import com.robocore.qleaptemi.UiEvent
import com.robocore.qleaptemi.UiState
import com.robocore.qleaptemi.inject.MqttCoroutineScope
import com.robocore.qleaptemi.mqtt.MqttConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mqttConnection: MqttConnection,
    @MqttCoroutineScope private val externalScope: CoroutineScope
) :
    BaseViewModel<HomeViewModel.State, HomeViewModel.Event, HomeViewModel.Effect>() {

    init {
        viewModelScope.launch {
            mqttConnection.status.collect {
                setState { copy(mqttConnectionStatus = it) }
            }
        }
    }

    override fun createInitialState(): State {
        return State()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.ConnectMqttTest -> {
                viewModelScope.launch {
                    mqttConnection.connect()
                    Log.d(TAG, externalScope.toString())
                }
            }
            is Event.StartOrStopQLeap -> {
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
            }
            is Event.OpenOrCloseSettings -> {

            }
            is Event.OpenOrCloseAdminSettings -> {

            }
        }
    }

    // Ui View States
    data class State(
        val mqttConnectionStatus: MqttConnection.ConnectionStatus = MqttConnection.ConnectionStatus.NONE,
    ) : UiState

    // Events that user performed
    sealed class Event : UiEvent {
        object ConnectMqttTest : Event()
        object StartOrStopQLeap : Event()
        object OpenOrCloseSettings : Event()
        object OpenOrCloseAdminSettings : Event()
    }

    // Side effects
    sealed class Effect : UiEffect {
        object ShowToast : Effect()

    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}
