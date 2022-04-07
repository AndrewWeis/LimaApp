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
class TimerDataStore @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.createDataStore("timer_data_store")

    val previousTimerLengthSeconds: Flow<Long>
        get() = dataStore.data.map { preferences ->
            preferences[TimerKeys.PREVIOUS_TIMER_LENGTH_SECONDS] ?: -1
        }

    val secondsRemaining: Flow<Long>
        get() = dataStore.data.map { preferences ->
            preferences[TimerKeys.SECONDS_REMAINING] ?: -1
        }

    val timerState: Flow<Int>
        get() = dataStore.data.map { preferences ->
            preferences[TimerKeys.TIMER_STATE] ?: -1
        }

    val alarmSetTime: Flow<Long>
        get() = dataStore.data.map { preferences ->
            preferences[TimerKeys.ALARM_SET_TIME] ?: -1
        }

    suspend fun setPreviousTimerLengthSeconds(lengthInSec: Long) {
        dataStore.edit { preferences ->
            preferences[TimerKeys.PREVIOUS_TIMER_LENGTH_SECONDS] = lengthInSec
        }
    }

    suspend fun setSecondsRemaining(secondsRemaining: Long) {
        dataStore.edit { preferences ->
            preferences[TimerKeys.SECONDS_REMAINING] = secondsRemaining
        }
    }

    suspend fun setTimerState(state: Int) {
        dataStore.edit { preferences ->
            preferences[TimerKeys.TIMER_STATE] = state
        }
    }

    suspend fun setAlarmSetTime(state: Long) {
        dataStore.edit { preferences ->
            preferences[TimerKeys.ALARM_SET_TIME] = state
        }
    }

    private object TimerKeys {
        val PREVIOUS_TIMER_LENGTH_SECONDS = preferencesKey<Long>("previous_timer_length_seconds")
        val SECONDS_REMAINING = preferencesKey<Long>("seconds_remaining")
        val TIMER_STATE = preferencesKey<Int>("timer_state")
        val ALARM_SET_TIME = preferencesKey<Long>("alarm_set_time")
    }
}
