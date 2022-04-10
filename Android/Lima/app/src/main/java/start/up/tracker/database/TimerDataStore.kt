package start.up.tracker.database

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import start.up.tracker.database.TimerDataStore.TimerKeys.DATA_STORE_NAME
import start.up.tracker.ui.fragments.pomodoro_timer.PomodoroTimer
import start.up.tracker.ui.fragments.pomodoro_timer.PomodoroTimerFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerDataStore @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.createDataStore(DATA_STORE_NAME)

    val secondsRemaining: Flow<Long>
        get() = dataStore.data.map { preferences ->
            preferences[TimerKeys.SECONDS_REMAINING] ?: PomodoroTimer.POMODORO_WORK_TIME
        }

    val timerState: Flow<Int>
        get() = dataStore.data.map { preferences ->
            preferences[TimerKeys.TIMER_STATE] ?: PomodoroTimer.TIMER_STATE_INITIAL
        }

    val timerIteration: Flow<Int>
        get() = dataStore.data.map { preferences ->
            preferences[TimerKeys.TIMER_ITERATION] ?: 0
        }

    val timerRestTime: Flow<Long>
        get() = dataStore.data.map { preferences ->
            preferences[TimerKeys.TIMER_REST_TIME] ?: PomodoroTimer.POMODORO_REST_SHORT
        }

    val timerMode: Flow<Int>
        get() = dataStore.data.map { preferences ->
            preferences[TimerKeys.TIMER_MODE] ?: PomodoroTimerFragment.CLOSEST_TASK_MODE
        }

    suspend fun saveSecondsRemaining(secondsRemaining: Long) {
        dataStore.edit { preferences ->
            preferences[TimerKeys.SECONDS_REMAINING] = secondsRemaining
        }
    }

    suspend fun saveTimerState(state: Int) {
        dataStore.edit { preferences ->
            preferences[TimerKeys.TIMER_STATE] = state
        }
    }

    suspend fun saveTimerIteration(iteration: Int) {
        dataStore.edit { preferences ->
            preferences[TimerKeys.TIMER_ITERATION] = iteration
        }
    }

    suspend fun saveRestTime(restTime: Long) {
        dataStore.edit { preferences ->
            preferences[TimerKeys.TIMER_REST_TIME] = restTime
        }
    }

    suspend fun saveTimerMode(mode: Int) {
        dataStore.edit { preferences ->
            preferences[TimerKeys.TIMER_MODE] = mode
        }
    }

    private object TimerKeys {
        const val DATA_STORE_NAME = "timer_data_store"

        val SECONDS_REMAINING = preferencesKey<Long>("seconds_remaining")
        val TIMER_STATE = preferencesKey<Int>("timer_state")
        val TIMER_ITERATION = preferencesKey<Int>("timer_iteration")
        val TIMER_REST_TIME = preferencesKey<Long>("timer_rest_time")
        val TIMER_MODE = preferencesKey<Int>("timer_mode")
    }
}
