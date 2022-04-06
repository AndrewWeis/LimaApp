package start.up.tracker.mvvm.view_models.pomodoro_timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import start.up.tracker.analytics.ActiveAnalytics
import start.up.tracker.analytics.principles.Pareto
import start.up.tracker.analytics.principles.Pomodoro
import start.up.tracker.database.TechniquesIds
import start.up.tracker.database.TimerDataStore
import start.up.tracker.entities.Task
import javax.inject.Inject

@HiltViewModel
class PomodoroTimerViewModel @Inject constructor(
    private val timerDataStore: TimerDataStore,
    private val activeAnalytics: ActiveAnalytics
) : ViewModel() {

    fun setPreviousTimerLengthSeconds(lengthInSec: Long) = viewModelScope.launch {
        timerDataStore.setPreviousTimerLengthSeconds(lengthInSec)
    }

    fun setSecondsRemaining(secondsRemaining: Long) = viewModelScope.launch {
        timerDataStore.setSecondsRemaining(secondsRemaining)
    }

    fun setTimerState(timerState: Int) = viewModelScope.launch {
        timerDataStore.setTimerState(timerState)
    }

    fun setAlarmSetTime(time: Long) = viewModelScope.launch {
        timerDataStore.setAlarmSetTime(time)
    }

    suspend fun getTimerState(): Int {
        return timerDataStore.timerState.first()
    }

    suspend fun getSecondsRemaining(): Long {
        return timerDataStore.secondsRemaining.first()
    }

    suspend fun getPreviousTimerLengthSeconds(): Long {
        return timerDataStore.previousTimerLengthSeconds.first()
    }

    suspend fun getAlarmSetTime(): Long {
        return timerDataStore.alarmSetTime.first()
    }

    suspend fun findTaskToMatch(): Task? {
        val a = activeAnalytics.getPrinciple(TechniquesIds.POMODORO) as Pomodoro
        return a.findTaskToMatch()
    }

    fun fromEndTimeToPomodoro(task : Task): Int? {
        val a = activeAnalytics.getPrinciple(TechniquesIds.POMODORO) as Pomodoro
        return a.fromEndTimeToPomodoro(task)
    }
}