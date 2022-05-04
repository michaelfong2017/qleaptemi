package com.robocore.qleaptemi.ui.settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.robocore.qleaptemi.BaseViewModel
import com.robocore.qleaptemi.UiEffect
import com.robocore.qleaptemi.UiEvent
import com.robocore.qleaptemi.UiState
import com.robocore.qleaptemi.settings.SettingsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsStore: SettingsStore,
) : BaseViewModel<SettingsViewModel.State, SettingsViewModel.Event, SettingsViewModel.Effect>() {
    init {
        viewModelScope.launch {
            settingsStore.volumePreference.collect {
                Log.d(TAG, "soundManager.volume.collect")
                setState { copy(volume = it) }
            }
        }
    }

    internal fun onVolumeSelected(selected: Int) {
        Log.d("onVolumeSelected", "selected: $selected")
        CoroutineScope(Dispatchers.IO).launch {
            settingsStore.setVolume(selected)
        }
    }

    override fun createInitialState(): State {
        return State()
    }

    override fun handleEvent(event: Event) {

    }

    // Ui View States
    data class State(
        val volume: Int = 0,
    ) : UiState

    // Events that user performed
    sealed class Event : UiEvent {
    }

    // Side effects
    sealed class Effect : UiEffect {
    }

    companion object {
        private const val TAG = "SettingsViewModel"
    }
}