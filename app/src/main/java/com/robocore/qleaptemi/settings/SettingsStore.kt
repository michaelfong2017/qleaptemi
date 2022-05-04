package com.robocore.qleaptemi.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsStore(private val context: Context) {
    /** DataStore */
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    val volumePreference: Flow<Int> = context.dataStore.data
        .map { preferences ->
            // No type safety.
            preferences[PREFERENCES_VOLUME] ?: initialVolume
        }

    /** DataStore END */


    suspend fun setVolume(selected: Int) {
        context.dataStore.edit { settings ->
            settings[PREFERENCES_VOLUME] = selected
        }
    }

    companion object {
        val PREFERENCES_VOLUME = intPreferencesKey("volume")
        const val initialVolume = 7
    }
}