package start.up.tracker.mvvm.view_models.pomodoro_timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import start.up.tracker.database.TimerDataStore
import start.up.tracker.ui.fragments.pomodoro_timer.PomodoroTimer
import javax.inject.Inject

@HiltViewModel
class PomodoroTimerViewModel @Inject constructor(
    private val timerDataStore: TimerDataStore,
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

    suspend fun getTimerState(): Int {
        return timerDataStore.timerState.first()
    }

    suspend fun getSecondsRemaining(): Long {
        return timerDataStore.secondsRemaining.first()
    }

    suspend fun getPreviousTimerLengthSeconds(): Long {
        return timerDataStore.previousTimerLengthSeconds.first()
    }
}