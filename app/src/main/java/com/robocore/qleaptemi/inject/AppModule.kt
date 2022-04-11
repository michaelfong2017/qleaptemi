package com.robocore.qleaptemi.inject

import android.content.Context
import com.robocore.qleaptemi.mqtt.MqttConnection
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
}