package start.up.tracker.mvvm.view_models.pomodoro_timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
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
    private val timerDataStore: TimerDataStore,
) : ViewModel() {

    private val timerEventChannel = Channel<TimerEvent>()
    val timerEvents = timerEventChannel.receiveAsFlow()

    val timer = PomodoroTimer(timerDataStore)

    private val _timerMode: MutableLiveData<Int> = MutableLiveData()
    val timerMode: LiveData<Int> get() = _timerMode

    private val _closestTask: MutableLiveData<Task?> = MutableLiveData()
    val closestTask: LiveData<Task?> = _closestTask

    init {
        setupTimerMode()
    }

    fun onClosestTaskModeSelected() = viewModelScope.launch {
        val pomodoro = activeAnalytics.principlesMap[TechniquesIds.POMODORO] as Pomodoro
        val tasks = pomodoro.getClosestTasksOfToday()

        if (tasks.isEmpty()) {
            _closestTask.postValue(null)
            return@launch
        }

        _closestTask.postValue(tasks.first())
    }

    fun onRestTimeButtonClick() = viewModelScope.launch {
        val restTime = timerDataStore.timerRestTime.first()
        timerEventChannel.send(TimerEvent.NavigateToRestTimeDialog(restTime))
    }

    fun onModeButtonClick() = viewModelScope.launch {
        val mode = timerDataStore.timerMode.first()
        timerEventChannel.send(TimerEvent.NavigateToModeDialog(mode))
    }

    fun handleTimerMode(timerMode: Int) = viewModelScope.launch {
        timerDataStore.saveTimerMode(timerMode)
        _timerMode.postValue(timerMode)
    }

    private fun setupTimerMode() = viewModelScope.launch {
        val savedMode = timerDataStore.timerMode.first()
        _timerMode.postValue(savedMode)
    }

    sealed class TimerEvent {
        data class NavigateToRestTimeDialog(val restTime: Long) : TimerEvent()
        data class NavigateToModeDialog(val mode: Int) : TimerEvent()
    }
}
