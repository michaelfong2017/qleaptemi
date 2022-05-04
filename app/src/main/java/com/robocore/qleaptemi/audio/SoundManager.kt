package com.robocore.qleaptemi.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import com.robocore.qleaptemi.settings.SettingsStore
import com.robocore.qleaptemi.settings.SettingsStore.Companion.initialVolume
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SoundManager(context: Context) : TextToSpeech.OnInitListener {
    /** Needed for field injection for a custom class
     * e.g. hiltEntryPoint.externalScope() provides the desired externalScope.
     */
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SoundManagerEntryPoint {
        fun settingsStore(): SettingsStore
    }

    private val hiltEntryPoint =
        EntryPointAccessors.fromApplication(context, SoundManagerEntryPoint::class.java)
    /** Needed for field injection for a custom class END */

    /** Audio */
    var tts = TextToSpeech(context, this)
        private set

    /** Volume 0-10 */
    private val _volume: MutableStateFlow<Int> = MutableStateFlow(initialVolume)
    val volume = _volume.asStateFlow()
    fun setVolume(volume: Int) {
        _volume.value = volume
    }

    /** Volume 0-10 END */

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val settingsStore = hiltEntryPoint.settingsStore()
            settingsStore.volumePreference.collectLatest {
                Log.d("volumePreference", "collect: $it")
                setVolume(it)
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            /** Set speech rate */
            tts.setSpeechRate(0.6f)
            /** Set speech rate END */


        } else {
            Log.e("SoundManager", "TTS Initialization failed!")
        }
    }
}