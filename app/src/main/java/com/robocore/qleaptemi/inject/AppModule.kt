package com.robocore.qleaptemi.inject

import android.content.Context
import com.robocore.qleaptemi.mqtt.MqttCallbackHandler
import com.robocore.qleaptemi.mqtt.MqttConnection
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
    fun provideWifiStatusReceiver(): WifiStatusReceiver =
        WifiStatusReceiver()
}