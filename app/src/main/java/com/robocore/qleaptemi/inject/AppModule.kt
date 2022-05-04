package com.robocore.qleaptemi.inject

import android.content.Context
import com.robocore.qleaptemi.audio.SoundManager
import com.robocore.qleaptemi.audio.VolumeReceiver
import com.robocore.qleaptemi.mqtt.MqttCallbackHandler
import com.robocore.qleaptemi.mqtt.MqttConnection
import com.robocore.qleaptemi.settings.SettingsStore
import com.robocore.qleaptemi.wifistatus.WifiStatusReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideMqttConnection(@ApplicationContext context: Context): MqttConnection =
        MqttConnection(context = context)

    @Singleton
    @Provides
    fun provideMqttCallbackHandler(@ApplicationContext context: Context): MqttCallbackHandler =
        MqttCallbackHandler(context = context)

    @Singleton
    @Provides
    fun provideWifiStatusReceiver(@ApplicationContext context: Context): WifiStatusReceiver =
        WifiStatusReceiver(context = context)

    @Singleton
    @Provides
    fun provideVolumeReceiver(@ApplicationContext context: Context): VolumeReceiver =
        VolumeReceiver(context = context)

    @Singleton
    @Provides
    fun provideSoundManager(@ApplicationContext context: Context): SoundManager =
        SoundManager(context = context)

    @Singleton
    @Provides
    fun provideSettingsStore(@ApplicationContext context: Context): SettingsStore =
        SettingsStore(context = context)
}