package start.up.tracker.database

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.createDataStore("user_preferences")

    val hideCompleted: Flow<Boolean>
        get() = dataStore.data.map { preferences ->
            preferences[PreferencesKeys.HIDE_COMPLETED] ?: false
        }

    val selectedTechniqueId: Flow<Int>
        get() = dataStore.data.map { preferences ->
            preferences[PreferencesKeys.SELECTED_TECHNIQUE_ID] ?: -1
        }

    suspend fun updateHideCompleted(hideCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HIDE_COMPLETED] = hideCompleted
        }
    }

    suspend fun updateTechniqueId(id: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_TECHNIQUE_ID] = id
        }
    }

    private object PreferencesKeys {
        val HIDE_COMPLETED = preferencesKey<Boolean>("hide_completed")
        val SELECTED_TECHNIQUE_ID = preferencesKey<Int>("selected_technique_id")
    }
}
