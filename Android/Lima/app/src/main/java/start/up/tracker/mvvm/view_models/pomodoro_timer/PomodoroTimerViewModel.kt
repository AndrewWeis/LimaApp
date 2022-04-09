package start.up.tracker.mvvm.view_models.pomodoro_timer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import start.up.tracker.analytics.ActiveAnalytics
import start.up.tracker.analytics.principles.Pomodoro
import start.up.tracker.database.TechniquesIds
import start.up.tracker.database.TimerDataStore
import start.up.tracker.entities.Task
import start.up.tracker.ui.fragments.pomodoro_timer.PomodoroTimer
import javax.inject.Inject

@HiltViewModel
class PomodoroTimerViewModel @Inject constructor(
    private val activeAnalytics: ActiveAnalytics,
    timerDataStore: TimerDataStore,
) : ViewModel() {

    val timer = PomodoroTimer(timerDataStore)

    suspend fun findTaskToMatch(): Task? {
        val pomodoro = activeAnalytics.getPrinciple(TechniquesIds.POMODORO) as Pomodoro
        return pomodoro.findTaskToMatch()
    }

    fun fromEndTimeToPomodoro(task: Task): Int? {
        val pomodoro = activeAnalytics.getPrinciple(TechniquesIds.POMODORO) as Pomodoro
        return pomodoro.fromEndTimeToPomodoro(task)
    }
}
