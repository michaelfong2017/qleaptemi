package com.robocore.qleaptemi.audio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VolumeReceiver(context: Context): BroadcastReceiver() {
    /** Needed for field injection for a custom class
     * e.g. hiltEntryPoint.externalScope() provides the desired externalScope.
     */
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface VolumeReceiverEntryPoint {
        fun soundManager(): SoundManager
    }

    private val hiltEntryPoint =
        EntryPointAccessors.fromApplication(context, VolumeReceiverEntryPoint::class.java)

    /** Needed for field injection for a custom class END */

    override fun onReceive(context: Context?, intent: Intent?) {
        val volumeAndroid: Int = intent?.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", -1) ?: -1
//        Log.d("VolumeReceiver", "volumeAndroid: $volumeAndroid")
        if (volumeAndroid != -1) {
            // 0-15
            val volume: Int = (volumeAndroid.toFloat() / 1.5f).toInt()
//            viewModel.updateVolume(volume)
//            locationEventHandler.setSpeakVolume(volume / 10f)
//            Log.d("VolumeReceiver", "volume: $volume")

            CoroutineScope(Dispatchers.IO).launch {
                val soundManager = hiltEntryPoint.soundManager()
                soundManager.setVolume(volume)
            }
        }
    }

}