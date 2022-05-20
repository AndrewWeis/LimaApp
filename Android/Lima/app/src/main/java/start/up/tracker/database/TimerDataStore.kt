package start.up.tracker.database

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import start.up.tracker.database.TimerDataStore.TimerKeys.DATA_STORE_NAME
import start.up.tracker.entities.Notification
import start.up.tracker.ui.fragments.pomodoro_timer.BaseTimer
import start.up.tracker.ui.fragments.pomodoro_timer.PomodoroTimer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerDataStore @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.createDataStore(DATA_STORE_NAME)

    val notification: Flow<Notification?>
        get() = dataStore.data.map { preferences ->
            preferences[TimerKeys.TIMER_NOTIFICATION]
        }

    val secondsRemaining: Flow<Long>
        get() = dataStore.data.map { preferences ->
            preferences[TimerKeys.SECONDS_REMAINING] ?: PomodoroTimer.POMODORO_WORK_TIME
        }

    val timerState: Flow<Int>
        get() = dataStore.data.map { preferences ->
            preferences[TimerKeys.TIMER_STATE] ?: BaseTimer.TIMER_STATE_INITIAL
        }

    val timerIteration: Flow<Int>
        get() = dataStore.data.map { preferences ->
            preferences[TimerKeys.TIMER_ITERATION] ?: 0
        }

    val timerRestTime: Flow<Long>
        get() = dataStore.data.map { preferences ->
            preferences[TimerKeys.TIMER_REST_TIME] ?: PomodoroTimer.POMODORO_REST_LONG
        }

    val timerMode: Flow<Int>
        get() = dataStore.data.map { preferences ->
            preferences[TimerKeys.TIMER_MODE] ?: PomodoroTimer.CLOSEST_TASK_MODE
        }

    val leftTime: Flow<Long>
        get() = dataStore.data.map { preferences ->
            preferences[TimerKeys.LEFT_TIME] ?: 0L
        }

    suspend fun saveNotification(notification: Notification) {
        dataStore.edit { preferences ->
            preferences[TimerKeys.TIMER_NOTIFICATION] = notification }
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

    suspend fun saveLeftTime(time: Long) {
        dataStore.edit { preferences ->
            preferences[TimerKeys.LEFT_TIME] = time
        }
    }

    private object TimerKeys {
        const val DATA_STORE_NAME = "timer_data_store"

        val SECONDS_REMAINING = preferencesKey<Long>("seconds_remaining")
        val TIMER_STATE = preferencesKey<Int>("timer_state")
        val TIMER_ITERATION = preferencesKey<Int>("timer_iteration")
        val TIMER_REST_TIME = preferencesKey<Long>("timer_rest_time")
        val TIMER_MODE = preferencesKey<Int>("timer_mode")
        val LEFT_TIME = preferencesKey<Long>("left_time")
        val TIMER_NOTIFICATION = preferencesKey<Notification>("timer_notification")
    }
}
