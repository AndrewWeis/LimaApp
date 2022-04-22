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
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.entities.Task
import start.up.tracker.ui.dialogs.pomodoro_timer.PomodoroTimer
import javax.inject.Inject

@HiltViewModel
class PomodoroTimerViewModel @Inject constructor(
    private val activeAnalytics: ActiveAnalytics,
    private val taskDao: TaskDao,
    private val timerDataStore: TimerDataStore,
) : ViewModel() {

    private val timerEventChannel = Channel<TimerEvent>()
    val timerEvents = timerEventChannel.receiveAsFlow()

    val timer = PomodoroTimer(timerDataStore)

    private val _closestTask: MutableLiveData<Task?> = MutableLiveData()
    val closestTask: LiveData<Task?> = _closestTask

    init {
        viewModelScope.launch {
            timer.recoverTimerState()
        }
    }

    fun onTimerStartButtonClicked() {
        timer.initCountDownTimer()
        timer.startTimer()
    }

    fun onTimerPauseButtonClicked() {
        timer.pauseTimer()
    }

    fun onTimerStopButtonClicked() {
        timer.stopTimer()
    }

    fun onContinueButtonClicked() {
        timer.continueTimer()
    }

    fun onSkipButtonClicked() {
        timer.skipTimer()
    }

    fun updateCompletedPomodoros(pomodoros: Int) = viewModelScope.launch {
        val closestTask = _closestTask.value?.copy(completedPomodoros = pomodoros)
        closestTask?.let {
            taskDao.updateTask(it)
            _closestTask.postValue(closestTask)
        }
    }

    fun onRestTimeButtonClicked() = viewModelScope.launch {
        val restTime = timerDataStore.timerRestTime.first()
        timerEventChannel.send(TimerEvent.NavigateToRestTimeDialog(restTime))
    }

    fun onModeButtonClicked() = viewModelScope.launch {
        val mode = timerDataStore.timerMode.first()
        timerEventChannel.send(TimerEvent.NavigateToModeDialog(mode))
    }

    fun onTimerModeChanged(timerMode: Int) = viewModelScope.launch {
        timerDataStore.saveTimerMode(timerMode)
        timer.setTimerMode(timerMode)
    }

    fun freeModeWasSelected(isInRestoreState: Boolean) {
        if (!isInRestoreState) {
            timer.freeModeWasSelected()
        }
    }

    fun closestTaskModeWasSelected(isInRestoreState: Boolean) = viewModelScope.launch {
        val pomodoro = activeAnalytics.principlesMap[TechniquesIds.POMODORO] as Pomodoro
        val tasks = pomodoro.getClosestTasksOfToday()

        if (tasks.isEmpty()) {
            _closestTask.postValue(null)
            return@launch
        }

        val closestTask = tasks.first()
        _closestTask.postValue(closestTask)

        if (!isInRestoreState) {
            timer.closestModeWasSelected(closestTask)
        }
    }

    fun onTimerRestTimeChanged(restTime: Long) = viewModelScope.launch {
        timerDataStore.saveRestTime(restTime)
        timer.restTime = restTime
    }

    fun getCompletedPomodoros(): Int? {
        return closestTask.value?.completedPomodoros
    }

    fun getTotalPomodoros(): Int? {
        return closestTask.value?.pomodoros
    }

    fun onClosestTaskCheckedChanged() = viewModelScope.launch {
        val closestTask = _closestTask.value?.copy(completed = true)
        closestTask?.let { taskDao.updateTask(it) }
        closestTaskModeWasSelected(false)
    }

    fun saveTimerState() = viewModelScope.launch {
        timer.saveTimerState()
    }

    sealed class TimerEvent {
        data class NavigateToRestTimeDialog(val restTime: Long) : TimerEvent()
        data class NavigateToModeDialog(val mode: Int) : TimerEvent()
    }
}
