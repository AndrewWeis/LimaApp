package start.up.tracker.mvvm.view_models.edit_task.dialogs

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.combine
import start.up.tracker.utils.screens.StateHandleKeys

class PomodorosViewModel(
    state: SavedStateHandle,
) : ViewModel() {

    private val currentStartTime = state.getLiveData<Int>(StateHandleKeys.CURRENT_START_TIME)
    private val currentPomodoros = state.getLiveData<Int>(StateHandleKeys.CURRENT_POMODOROS)

    val pomodorosData = currentPomodoros.asFlow()
        .combine(currentStartTime.asFlow()) { currentPomodoros, currentStartTime ->
            Pair(currentPomodoros, currentStartTime)
        }.asLiveData()

    fun onIndicatorProgressChanged(currentValue: Int) {
        currentPomodoros.postValue(currentValue)
    }

    fun onTimeStartChanged(time: Int) {
        currentStartTime.postValue(time)
    }
}
