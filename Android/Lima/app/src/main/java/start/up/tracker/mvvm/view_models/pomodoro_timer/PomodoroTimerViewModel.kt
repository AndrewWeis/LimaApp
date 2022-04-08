package start.up.tracker.mvvm.view_models.pomodoro_timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import start.up.tracker.analytics.ActiveAnalytics
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

    fun saveSecondsRemaining(secondsRemaining: Long) = viewModelScope.launch {
        timerDataStore.setSecondsRemaining(secondsRemaining)
    }

    fun saveTimerState(timerState: Int) = viewModelScope.launch {
        timerDataStore.setTimerState(timerState)
    }

    fun saveIteration(iteration: Int) = viewModelScope.launch {
        timerDataStore.setTimerIteration(iteration)
    }

    suspend fun getTimerState(): Int {
        return timerDataStore.timerState.first()
    }

    suspend fun getSecondsRemaining(): Long {
        return timerDataStore.secondsRemaining.first()
    }

    suspend fun getTimerIteration(): Int {
        return timerDataStore.timerIteration.first()
    }

    suspend fun findTaskToMatch(): Task? {
        val pomodoro = activeAnalytics.getPrinciple(TechniquesIds.POMODORO) as Pomodoro
        return pomodoro.findTaskToMatch()
    }

    fun fromEndTimeToPomodoro(task: Task): Int? {
        val pomodoro = activeAnalytics.getPrinciple(TechniquesIds.POMODORO) as Pomodoro
        return pomodoro.fromEndTimeToPomodoro(task)
    }
}
